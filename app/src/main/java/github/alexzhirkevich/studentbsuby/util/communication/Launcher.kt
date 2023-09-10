package github.alexzhirkevich.studentbsuby.util.communication

import kotlinx.coroutines.supervisorScope

interface Launcher
{
    suspend fun launch()

    companion object
    {
        fun combine(vararg launcher: Launcher): Launcher = object : Launcher
        {
            override suspend fun launch()
            {
                launcher.forEach {
                    supervisorScope {
                        it.launch()
                    }
                }
            }
        }
    }
}