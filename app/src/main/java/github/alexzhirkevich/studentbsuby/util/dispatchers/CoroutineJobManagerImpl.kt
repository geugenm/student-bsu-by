package github.alexzhirkevich.studentbsuby.util.dispatchers

import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap

class CoroutineJobManagerImpl : CoroutineJobManager
{

    private val runningJobs: MutableMap<Any?, Job> = ConcurrentHashMap()


    override fun cancel(key: Any?)
    {
        if (key != null)
        {
            runningJobs.remove(key)?.cancel()
        }
    }

    override fun save(job: Job, key: Any?)
    {
        if (key != null)
        {
            runningJobs[key] = job
            job.invokeOnCompletion { runningJobs.remove(key) }
        }
    }
}