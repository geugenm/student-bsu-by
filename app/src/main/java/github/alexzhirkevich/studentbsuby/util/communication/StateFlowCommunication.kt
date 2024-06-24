package github.alexzhirkevich.studentbsuby.util.communication

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

class StateFlowCommunication<T>(
    initial: T, private val serializer: Serializer<T> = PrimitiveSerializer()
                               ) : MutableStateCommunication<T>
{

    override val current: T get() = flow.value

    private val flow = MutableStateFlow(initial)
    private var currentSaveStateHandle: SavedStateHandle? = null
    private var currentSaveStateHandleKey: String? = null

    override fun map(data: T)
    {

        flow.tryEmit(data)
        currentSaveStateHandle?.let {
            currentSaveStateHandleKey?.let { key ->
                it[key] = serializer.serialize(data)
            }
        }
    }

    override suspend fun collect(collector: suspend (T) -> Unit)
    {
        flow.collectLatest(collector)
    }

    override suspend fun saveIn(savedStateHandle: SavedStateHandle, key: String)
    {
        currentSaveStateHandle = savedStateHandle
        currentSaveStateHandleKey = key

        savedStateHandle.get<Bundle>(key)?.let(serializer::deserialize)?.let {
            map(it)
        } ?: run {
            savedStateHandle[key] = serializer.serialize(current)
        }
    }
}
