package github.alexzhirkevich.studentbsuby.api

import androidx.annotation.IntRange
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.http.*

fun TimetableApi.dayOfWeek(@IntRange(from = 0, to = 5) day: Int): FormUrlEncodedBody
{
    val eventTarget =
        "ctl00\$ctl00\$ContentPlaceHolder0\$ContentPlaceHolder1\$ctlSchedule1\$cmdDay${day + 1}"
    val scriptManager =
        "ctl00\$ctl00\$ContentPlaceHolder0\$ContentPlaceHolder1\$ctlSchedule1\$ScriptManager1"

    return mapOf(
        scriptManager to "$eventTarget|${scriptManager}", "__EVENTTARGET" to eventTarget
                )
}

interface TimetableApi
{

    @Headers("User-Agent: Mozilla")
    @GET("PersonalCabinet/Schedule")
    suspend fun init(): Response<ResponseBody>

    @FormUrlEncoded
    @Headers("User-Agent: Mozilla")
    @POST("PersonalCabinet/Schedule")
    suspend fun timetable(
        @FieldMap(encoded = false) dayOfWeek: FormUrlEncodedBody
                         ): Response<ResponseBody>
}

class TimetableApiWrapper(private val api: TimetableApi) : TimetableApi
{

    private var __VIEWSTATE = ""
    private var __VIEWSTATEGENERATOR = ""
    private var __EVENTVALIDATION = ""
    private var __BTNLOGON = ""
    private var __EVENTARGUMENT = ""

    override suspend fun init(): Response<ResponseBody>
    {
        val resp = api.init()

        val jsoup = resp.body()?.byteStream()?.readBytes()?.let {
            Jsoup.parse(String(it))
        }
        __EVENTARGUMENT = jsoup?.getElementById("__EVENTARGUMENT")?.attr("value").orEmpty()
        __VIEWSTATE = jsoup?.getElementById("__VIEWSTATE")?.attr("value").orEmpty()
        __VIEWSTATEGENERATOR =
            jsoup?.getElementById("__VIEWSTATEGENERATOR")?.attr("value").orEmpty()
        __EVENTVALIDATION = jsoup?.getElementById("__EVENTVALIDATION")?.attr("value").orEmpty()
        __BTNLOGON =
            jsoup?.getElementsByAttributeValue("name", "ctl00\$ContentPlaceHolder0\$btnLogon")
                ?.attr("value") ?: "Войти"
        return resp
    }

    override suspend fun timetable(dayOfWeek: FormUrlEncodedBody): Response<ResponseBody>
    {
        val map = dayOfWeek.toMutableMap().apply {
            this["__EVENTARGUMENT"] = __EVENTARGUMENT
            this["__VIEWSTATE"] = __VIEWSTATE
            this["__VIEWSTATEGENERATOR"] = __VIEWSTATEGENERATOR
            this["__EVENTVALIDATION"] = __EVENTVALIDATION
            this["ctl00\$ContentPlaceHolder0\$btnLogon"] = __BTNLOGON
            this["__ASYNCPOST"] = "true"
        }
        return api.timetable(map)
    }

}