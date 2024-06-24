package github.alexzhirkevich.studentbsuby.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

val ProfileApi.AllSubjectsRequest: FormUrlEncodedBody
    get() = mapOf(
        "ctl00%24ctl00%24ContentPlaceHolder0%24ContentPlaceHolder1%24ctlStudProgress1%24ScriptManager1" to "ctl00%24ctl00%24ContentPlaceHolder0%24ContentPlaceHolder1%24ctlStudProgress1%24UpdatePanel1%7Cctl00%24ctl00%24ContentPlaceHolder0%24ContentPlaceHolder1%24ctlStudProgress1%24selSemester",
        "__EVENTTARGET" to "ctl00%24ctl00%24ContentPlaceHolder0%24ContentPlaceHolder1%24ctlStudProgress1%24selSemester",
        "__EVENTARGUMENT" to "",
        "__VIEWSTATE" to "%2FwEPDwUJODcyNjAzNTYyD2QWAmYPZBYCZg9kFgICAw9kFghmDw8WAh4HVmlzaWJsZWdkZAIBDw8WAh4LTmF2aWdhdGVVcmwFEX4vUGVyc29uYWxDYWJpbmV0ZGQCAw9kFgJmD2QWAgIBDw8WAh4EVGV4dAUy0JbQuNGA0LrQtdCy0LjRhyDQkNC70LXQutGB0LDQvdC00YAg0K7RgNGM0LXQstC40YdkZAIED2QWFAIDDw8WBB4JRm9udF9Cb2xkZx4EXyFTQgKAEGRkAgsPDxYCHwBoZGQCDQ8PFgIfAGhkZAIPDw8WAh8AaGRkAhEPDxYCHwBoZGQCEw8PFgIfAGhkZAIVDw8WAh8AaGRkAh8PDxYCHwBnZBYCAgMPEGQPFgNmAgECAhYDEAUFINC00LAFATFnEAUHINC90LXRggUBMmcQBRog0L3QtSDQuNC90YLQtdGA0LXRgdGD0LXRggUBM2dkZAIhDw8WAh8CBTLQltC40YDQutC10LLQuNGHINCQ0LvQtdC60YHQsNC90LTRgCDQrtGA0YzQtdCy0LjRh2RkAiUPZBYCAgEPZBYCAgMPZBYCZg9kFgwCAQ8PFgIfAgVW0KTQsNC60YPQu9GM0YLQtdGCINC%2F0YDQuNC60LvQsNC00L3QvtC5INC80LDRgtC10LzQsNGC0LjQutC4INC4INC40L3RhNC%2B0YDQvNCw0YLQuNC60LhkZAIDDw8WAh8CBeoBMyDQutGD0YDRgSwg0LPRgNGD0L%2FQv9CwIDksINGE0L7RgNC80LAg0L7QsdGD0YfQtdC90LjRjyDQtNC90LXQstC90LDRjywg0YHQv9C10YbQuNCw0LvRjNC90L7RgdGC0Yw6INC60L7QvNC%2F0YzRjtGC0LXRgNC90LDRjyDQsdC10LfQvtC%2F0LDRgdC90L7RgdGC0YwgKNC80LDRgtC10LzQsNGC0LjRh9C10YHQutC40LUg0LzQtdGC0L7QtNGLINC4INC%2F0YDQvtCz0YDQsNC80LzQvdGL0LUg0YHQuNGB0YLQtdC80YspZGQCBQ8PFgIfAgUjPGI%2B0YHRgNC10LTQvdC40Lkg0LHQsNC70Ls6IDcsODwvYj5kZAIHDxBkDxYHZgIBAgICAwIEAgUCBhYHEAUT0JLRgdC1INGB0LXRgdGB0LjQuAUBMGcQBSUxINC60YPRgNGBLCDQt9C40LzQvdGP0Y8g0YHQtdGB0YHQuNGPBQExZxAFKTEg0LrRg9GA0YEsINCy0LXRgdC10L3QvdGP0Y8g0YHQtdGB0YHQuNGPBQEyZxAFJTIg0LrRg9GA0YEsINC30LjQvNC90Y%2FRjyDRgdC10YHRgdC40Y8FATNnEAUpMiDQutGD0YDRgSwg0LLQtdGB0LXQvdC90Y%2FRjyDRgdC10YHRgdC40Y8FATRnEAUlMyDQutGD0YDRgSwg0LfQuNC80L3Rj9GPINGB0LXRgdGB0LjRjwUBNWcQBSkzINC60YPRgNGBLCDQstC10YHQtdC90L3Rj9GPINGB0LXRgdGB0LjRjwUBNmcWAQIFZAIJDw8WBB8DaB8EAoAQZGQCCw9kFgJmD2QWBmYPFgIeBWNsYXNzBQ9zdHlsZVNlc3Npb25TZWxkAgEPFgIfBQUPc3R5bGVTZXNzaW9uU2VsZAICDxYCHwUFD3N0eWxlU2Vzc2lvblNlbGQYAgUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgIFKWN0bDAwJGN0bDAwJExvZ2luVmlldzEkTG9naW5TdGF0dXMyJGN0bDAxBSljdGwwMCRjdGwwMCRMb2dpblZpZXcxJExvZ2luU3RhdHVzMiRjdGwwMwUWY3RsMDAkY3RsMDAkTG9naW5WaWV3MQ8PZAIBZCW4qCnTNihNSIt5haB8L5lOp8kDrzKYwMcx%2FkiM1rfS",
        "__VIEWSTATEGENERATOR" to "81FD4C30",
        "__EVENTVALIDATION" to "%2FwEdAA8aA1dTk1mI0qHq2cWHuAX0uANDprxA9HupiwJWIpYTdhQdutRiZ%2FLTn4Mka6ETO2i2RziiPuZr2eU64bYcxS9FKF4OnhBxwP4fhl1lRE3aOSIAP%2FaYJhBsXRIrQyPdHZmZapxzn2aqKpUNs4OcstQtbUJ3ThzZBUk9RXwMyFtZCnGZrP87pOhw12KtU4lbdov8%2FxtrIqZi18sjUwGvf5sc8FAHVG7rNEhT97HHjTgvcof5j6YHrBXu2NGUtQ7dQYKDtwDTLUJNmzM%2BbRHvQgUr4e97YuQ%2FMJCYwyRhyG%2BcdCPAxn6wNsBGgLmKmXfIG3dl9zaWNmpk3Sfikcbtf3mAVcBZ9po87HmV%2FYMBtbJ0PA%3D%3D",
        "ctl00%24ctl00%24ContentPlaceHolder0%24Poll1%24rbAnswers" to "1",
        "__ASYNCPOST" to "true"
                 )

interface ProfileApi
{

    @Streaming
    @GET("Photo/Photo.aspx")
    suspend fun photo(): Response<ResponseBody>

    @GET("PersonalCabinet/StudProgress")
    suspend fun studProgress(): Response<ResponseBody>

    @FormUrlEncoded
    @Headers("User-Agent: Mozilla")
    @POST("PersonalCabinet/StudProgress")
    suspend fun subjects(
        @FieldMap(encoded = true) request: FormUrlEncodedBody
                        ): Response<ResponseBody>

    @GET(URL_NEWS)
    suspend fun newsItem(@Query(value = "id") id: Int): Response<ResponseBody>

    @GET(URL_NEWS)
    suspend fun news(): Response<ResponseBody>

    @GET("PersonalCabinet/Hostel")
    suspend fun hostel(): Response<ResponseBody>

    @GET("PersonalCabinet/stb")
    suspend fun studentTicket(): Response<ResponseBody>

    suspend fun exit(): Response<ResponseBody>

    companion object
    {
        const val URL_NEWS = "PersonalCabinet/News"
    }
}