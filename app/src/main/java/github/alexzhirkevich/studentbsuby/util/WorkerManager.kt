package github.alexzhirkevich.studentbsuby.util

interface WorkerManager : Runnable
{

    suspend fun isEnabled(): Boolean

    fun stop()
}