package github.alexzhirkevich.studentbsuby.api

import androidx.annotation.IntRange
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

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
        val response = api.init()
        response.body()?.byteStream()?.use { stream ->
            val document = Jsoup.parse(stream.readBytes().toString(Charsets.UTF_8))

            __EVENTARGUMENT = document.getElementById("__EVENTARGUMENT")?.attr("value").orEmpty()
            __VIEWSTATE = document.getElementById("__VIEWSTATE")?.attr("value").orEmpty()
            __VIEWSTATEGENERATOR =
                document.getElementById("__VIEWSTATEGENERATOR")?.attr("value").orEmpty()
            __EVENTVALIDATION =
                document.getElementById("__EVENTVALIDATION")?.attr("value").orEmpty()
            __BTNLOGON = document.selectFirst("input[name=ctl00\$ContentPlaceHolder0\$btnLogon]")
                ?.attr("value") ?: "Войти"
        }
        return response
    }

    override suspend fun timetable(dayOfWeek: FormUrlEncodedBody): Response<ResponseBody>
    {
        val form = dayOfWeek.toMutableMap().apply {
            put("__EVENTARGUMENT", __EVENTARGUMENT)
            put("__VIEWSTATE", __VIEWSTATE)
            put("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
            put("__EVENTVALIDATION", __EVENTVALIDATION)
            put("ctl00\$ContentPlaceHolder0\$btnLogon", __BTNLOGON)
            put("__ASYNCPOST", "true")
        }
        return api.timetable(form)
    }
}