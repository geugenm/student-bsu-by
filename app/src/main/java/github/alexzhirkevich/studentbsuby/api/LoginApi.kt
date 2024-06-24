package github.alexzhirkevich.studentbsuby.api

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

fun LoginApi.createLoginData(
    login: String, stud: String, captcha: String
                            ): FormUrlEncodedBody = mapOf(
    "ctl00\$ContentPlaceHolder0\$txtUserLogin" to login,
    "ctl00\$ContentPlaceHolder0\$txtUserPassword" to stud,
    "ctl00\$ContentPlaceHolder0\$txtCapture" to captcha,
                                                         )


interface LoginApi
{

    @GET("login.aspx")
    suspend fun initialize(): Response<ResponseBody>

    @Streaming
    @GET("Captcha/CaptchaImage.aspx")
    suspend fun captcha(): Response<ResponseBody>

    @FormUrlEncoded
    @POST("login.aspx")
    suspend fun login(
        @FieldMap(encoded = false) body: FormUrlEncodedBody
                     ): Response<ResponseBody>
}

val ResponseBody.isSessionExpired: Boolean
    get() = contains("ctl00_ContentPlaceHolder0_cmdLogIn") || contains("ctl00\$ContentPlaceHolder0\$btnLogon")

private fun ResponseBody.contains(text: String): Boolean =
    source().buffer.clone().readString(Charsets.UTF_8).contains(text)

class LoginApiWrapper(val api: LoginApi) : LoginApi
{

    private var __VIEWSTATE = ""
    private var __VIEWSTATEGENERATOR = ""
    private var __EVENTVALIDATION = ""
    private var __BTNLOGON = ""
    private var __EVENTTARGET = ""
    private var __EVENTARGUMENT = ""

    override suspend fun initialize(): Response<ResponseBody>
    {
        val response = api.initialize()
        response.body()?.byteStream()?.use { stream ->
            val document = Jsoup.parse(stream.readBytes().toString(Charsets.UTF_8))

            __EVENTARGUMENT = document.getElementById("__EVENTARGUMENT")?.attr("value").orEmpty()
            __EVENTTARGET = document.getElementById("__EVENTTARGET")?.attr("value").orEmpty()
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

    override suspend fun captcha(): Response<ResponseBody>
    {
        return api.captcha()
    }

    override suspend fun login(
        @FieldMap(encoded = false) body: FormUrlEncodedBody
                              ): Response<ResponseBody>
    {
        val form = body.toMutableMap().apply {
            put("__EVENTTARGET", __EVENTTARGET)
            put("__EVENTARGUMENT", __EVENTARGUMENT)
            put("__VIEWSTATE", __VIEWSTATE)
            put("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
            put("__EVENTVALIDATION", __EVENTVALIDATION)
            put("ctl00\$ContentPlaceHolder0\$btnLogon", __BTNLOGON)
        }
        return api.login(form)
    }
}