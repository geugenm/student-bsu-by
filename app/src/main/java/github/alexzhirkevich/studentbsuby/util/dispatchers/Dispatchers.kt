package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext


/**
 * Coroutine dispatchers wrapper for simplifying testing
 * */
interface Dispatchers : UIDispatcher, IODispatcher, ComputationalDispatcher
{

    /**
     * Uses default [kotlinx.coroutines.Dispatchers]
     * */
    class Base : Dispatchers
    {

        override fun launchUI(
            scope: CoroutineScope,
            key: Any?,
            exceptionHandler: CoroutineExceptionHandler?,
            block: suspend CoroutineScope.() -> Unit
                             ): Job = scope.launchWithKey(
            kotlinx.coroutines.Dispatchers.Main.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, key, block
                                                         )

        override fun launchComputation(
            scope: CoroutineScope,
            key: Any?,
            exceptionHandler: CoroutineExceptionHandler?,
            block: suspend CoroutineScope.() -> Unit
                                      ): Job = scope.launchWithKey(
            kotlinx.coroutines.Dispatchers.Default.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, key, block
                                                                  )

        override fun launchIO(
            scope: CoroutineScope,
            key: Any?,
            exceptionHandler: CoroutineExceptionHandler?,
            block: suspend CoroutineScope.() -> Unit
                             ): Job = scope.launchWithKey(
            kotlinx.coroutines.Dispatchers.IO.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, key, block
                                                         )

        override suspend fun <T> runOnUI(
            exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                        ) = withContext(
            kotlinx.coroutines.Dispatchers.Main.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, block = block
                                                       )

        override suspend fun <T> runOnComputational(
            exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                                   ) = withContext(
            kotlinx.coroutines.Dispatchers.Default.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, block = block
                                                                  )


        override suspend fun <T> runOnIO(
            exceptionHandler: CoroutineExceptionHandler?, block: suspend CoroutineScope.() -> T
                                        ) = withContext(
            kotlinx.coroutines.Dispatchers.IO.let {
                if (exceptionHandler != null) it + exceptionHandler else it
            }, block = block
                                                       )

        private val runningJobs: MutableMap<Any?, Job> = ConcurrentHashMap()

        private fun closeJob(key: Any?)
        {
            if (key != null)
            {
                runningJobs.remove(key)?.cancel()
            }
        }

        private fun Job.save(key: Any?): Job = also {
            if (key != null)
            {
                runningJobs[key] = it
                it.invokeOnCompletion { runningJobs.remove(key) }
            }
        }

        private fun CoroutineScope.launchWithKey(
            context: CoroutineContext,
            key: Any?,
            block: suspend CoroutineScope.() -> Unit,
                                                ): Job
        {
            closeJob(key)
            return launch(context, block = block).save(key)
        }
    }
}