package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Dispatcher for
 * */
interface ComputationalDispatcher
{

    /**
     * Launches given [block] in [scope] with dispatcher defined as Computation.
     * Launching with non-null [key] cancels previous job with the same [key] if it is in progress.
     * @return [Job] of the launched coroutine.
     * */
    fun launchComputation(
        scope: CoroutineScope,
        key: Any? = null,
        exceptionHandler: CoroutineExceptionHandler? = null,
        block: suspend CoroutineScope.() -> Unit,
                         ): Job

    /**
     * Switches dispatcher to Computational.
     * */
    suspend fun <T> runOnComputational(
        exceptionHandler: CoroutineExceptionHandler? = null, block: suspend CoroutineScope.() -> T
                                      ): T
}