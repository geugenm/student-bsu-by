package github.alexzhirkevich.studentbsuby.util

import github.alexzhirkevich.studentbsuby.util.logger.Logger
import github.alexzhirkevich.studentbsuby.util.logger.TaggedException
import kotlinx.coroutines.CoroutineExceptionHandler

interface ErrorHandler
{

    fun handle(error: Throwable)

    class Log(private val logger: Logger) : ErrorHandler
    {
        override fun handle(error: Throwable)
        {
            val (tag, cause) = if (error is TaggedException) error.message.orEmpty() to error.cause
            else "" to error

            logger.log(
                msg = "Unhandled error occurred",
                tag = tag,
                cause = cause,
                logLevel = Logger.LogLevel.Error
                      )
        }
    }
}

fun ErrorHandler.toCoroutineExceptionHandler(): CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, throwable ->
        handle(throwable)
    }