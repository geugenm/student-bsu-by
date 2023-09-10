package github.alexzhirkevich.studentbsuby.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface PaidServicesApi
{

    @GET("PersonalCabinet/Pay/Info")
    suspend fun info(): Response<ResponseBody>

    @GET("PersonalCabinet/Pay/report")
    @Headers("User-Agent: Mozilla")
    suspend fun tuitionFee(): Response<ResponseBody>

    @GET("PersonalCabinet/Pay/reportAkadem")
    @Headers("User-Agent: Mozilla")
    suspend fun academicDebt(): Response<ResponseBody>

    @GET("PersonalCabinet/Pay/HousePay")
    @Headers("User-Agent: Mozilla")
    suspend fun hostelBills(): Response<ResponseBody>

    @GET("PersonalCabinet/Pay/reportOther")
    @Headers("User-Agent: Mozilla")
    suspend fun common(): Response<ResponseBody>
}