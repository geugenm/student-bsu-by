package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.Job

fun interface CoroutineJobSaver
{
    fun save(job: Job, key: Any?)
}