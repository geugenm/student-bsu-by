package github.alexzhirkevich.studentbsuby.repo

import github.alexzhirkevich.studentbsuby.api.isSessionExpired
import github.alexzhirkevich.studentbsuby.util.exceptions.EmptyResponseException
import github.alexzhirkevich.studentbsuby.util.exceptions.FailResponseException
import github.alexzhirkevich.studentbsuby.util.exceptions.SessionExpiredException
import okhttp3.ResponseBody
import retrofit2.Response

@Throws(
    FailResponseException::class, SessionExpiredException::class, EmptyResponseException::class
       )
fun Response<ResponseBody>.html(): String
{
    return String(bytes())
}

@Throws(
    FailResponseException::class, SessionExpiredException::class, EmptyResponseException::class
       )
fun Response<ResponseBody>.bytes(): ByteArray
{
    if (!isSuccessful) throw FailResponseException(code())
    if (body()?.isSessionExpired == true) throw SessionExpiredException()

    return body()?.byteStream()?.readBytes() ?: throw EmptyResponseException()
}