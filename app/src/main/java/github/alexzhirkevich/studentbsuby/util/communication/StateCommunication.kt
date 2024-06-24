package github.alexzhirkevich.studentbsuby.util.communication

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

interface StateCommunication<T> : Communication<T>, StateHolder<T>

fun <T> Communication<T>.toStateCommunication(initial: T): StateCommunication<T> =
    object : StateCommunication<T>
    {

        private var currentSaveStateHandle: SavedStateHandle? = null
        private var currentSaveStateHandleKey: String? = null

        private val saveStateHandleFlow = MutableStateFlow(initial)

        override suspend fun collect(collector: suspend (T) -> Unit)
        {
            listOf(flow {
                this@toStateCommunication.collect {
                    current = it
                    currentSaveStateHandle?.let {
                        currentSaveStateHandleKey?.let { key ->
                            it[key] = current
                        }
                    }
                    emit(it)
                }
            }, saveStateHandleFlow).merge().collect {
                collector(it)
            }
        }

        override var current: T = initial
            private set

        override suspend fun saveIn(savedStateHandle: SavedStateHandle, key: String)
        {

            savedStateHandle.get<T>(key)?.let {
                current = it
                saveStateHandleFlow.tryEmit(it)
            }
        }
    }

fun <T> StateFlow<T>.toStateCommunication() = toCommunication().toStateCommunication(value)