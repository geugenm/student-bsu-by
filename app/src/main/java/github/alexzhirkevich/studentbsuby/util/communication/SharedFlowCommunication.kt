package github.alexzhirkevich.studentbsuby.util.communication

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class SharedFlowCommunication<T> : MutableCommunication<T>
{

    private val flow = MutableSharedFlow<T>(
        extraBufferCapacity = 3, onBufferOverflow = BufferOverflow.DROP_OLDEST
                                           )

    override fun map(data: T)
    {
        flow.tryEmit(data)
    }

    override suspend fun collect(collector: suspend (T) -> Unit) = flow.collect { collector(it) }
}
