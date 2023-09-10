package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface IODispatcher
{

    /**
     * Launches given [block] in [scope] with dispatcher defined as Input-Output.
     * Launching with non-null [key] cancels previous job with the same [key] if it is in progress.
     * @return [Job] of the launched coroutine.
     * */
    fun launchIO(
        scope: CoroutineScope,
        key: Any? = null,
        exceptionHandler: CoroutineExceptionHandler? = null,
        block: suspend CoroutineScope.() -> Unit,
                ): Job

    /**
     * Switches dispatcher to IO.
     * */
    suspend fun <T> runOnIO(
        exceptionHandler: CoroutineExceptionHandler? = null, block: suspend CoroutineScope.() -> T
                           ): T
}