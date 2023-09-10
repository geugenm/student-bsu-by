package github.alexzhirkevich.studentbsuby.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.CoroutineScope

open class SuspendHandlerViewModel<T : Event>(
    private val dispatchers: Dispatchers,
    private val suspendEventHandler: SuspendEventHandler<T>,
    private val errorHandler: ErrorHandler
                                             ) : ViewModel(), EventHandler<T>
{

    init
    {
        launch {
            suspendEventHandler.launch()
        }
    }

    final override fun handle(event: T) = launch {
        suspendEventHandler.handle(event)
    }

    override fun onCleared()
    {
        super.onCleared()
        suspendEventHandler.release()
    }


    private fun launch(block: suspend CoroutineScope.() -> Unit)
    {
        dispatchers.launchIO(
            viewModelScope,
            exceptionHandler = errorHandler.toCoroutineExceptionHandler(),
            block = block
                            )
    }
}