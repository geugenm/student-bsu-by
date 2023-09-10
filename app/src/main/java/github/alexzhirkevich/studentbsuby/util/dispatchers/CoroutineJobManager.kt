package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface CoroutineJobManager : CoroutineJobSaver, CoroutineJobCancel

fun CoroutineJobManager.launch(
    scope: CoroutineScope,
    context: CoroutineContext,
    key: Any?,
    block: suspend CoroutineScope.() -> Unit,
                              ): Job
{
    cancel(key)
    return scope.launch(context, block = block).also {
        save(it, key)
    }
}

