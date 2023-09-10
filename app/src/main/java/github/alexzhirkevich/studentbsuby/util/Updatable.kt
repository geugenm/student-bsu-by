package github.alexzhirkevich.studentbsuby.util

import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Updatable
{

    val isUpdating: StateCommunication<Boolean>

    fun update()
}

suspend fun setState(block: () -> Unit) = withContext(Dispatchers.Main) { block() }