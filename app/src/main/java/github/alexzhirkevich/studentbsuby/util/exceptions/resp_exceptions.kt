package github.alexzhirkevich.studentbsuby.util.exceptions

import okio.IOException


open class NetworkResponseException : IOException()

class FailResponseException(val code: Int) : NetworkResponseException()

class EmptyResponseException : NetworkResponseException()

class IncorrectResponseException : NetworkResponseException()

class SessionExpiredException : NetworkResponseException()