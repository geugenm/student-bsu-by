package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class DispatchersImpl(
    private val jobManager: CoroutineJobManager
                     ) : Dispatchers
{

    override fun launchUI(
        scope: CoroutineScope,
        key: Any?,
        exceptionHandler: CoroutineExceptionHandler?,
        block: suspend CoroutineScope.() -> Unit
                         ): Job = jobManager.launch(
        scope = scope, context = kotlinx.coroutines.Dispatchers.Main.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, key = key, block = block
                                                   )

    override fun launchComputation(
        scope: CoroutineScope,
        key: Any?,
        exceptionHandler: CoroutineExceptionHandler?,
        block: suspend CoroutineScope.() -> Unit
                                  ): Job = jobManager.launch(
        scope = scope, context = kotlinx.coroutines.Dispatchers.Default.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, key = key, block = block
                                                            )

    override fun launchIO(
        scope: CoroutineScope,
        key: Any?,
        exceptionHandler: CoroutineExceptionHandler?,
        block: suspend CoroutineScope.() -> Unit
                         ): Job = jobManager.launch(
        scope = scope, context = kotlinx.coroutines.Dispatchers.IO.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, key = key, block = block
                                                   )

    override suspend fun <T> runOnUI(
        exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                    ) = withContext(
        context = kotlinx.coroutines.Dispatchers.Main.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, block = block
                                                   )

    override suspend fun <T> runOnComputational(
        exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                               ) = withContext(
        context = kotlinx.coroutines.Dispatchers.Default.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, block = block
                                                              )

    override suspend fun <T> runOnIO(
        exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                    ) = withContext(
        context = kotlinx.coroutines.Dispatchers.IO.let {
            if (exceptionHandler != null) it + exceptionHandler else it
        }, block = block
                                                   )
}

