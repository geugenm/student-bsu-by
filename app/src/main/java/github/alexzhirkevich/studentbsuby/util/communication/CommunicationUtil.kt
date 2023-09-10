package github.alexzhirkevich.studentbsuby.util.communication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> Communication<T>.collectAsState(
    initial: T, context: CoroutineContext = EmptyCoroutineContext
                                       ) = produceState(initial, this, context) {
    if (context == EmptyCoroutineContext)
    {
        collect { value = it }
    } else withContext(context) {
        collect { value = it }
    }
}

@Composable
fun <T> StateCommunication<T>.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
                                            ): State<T> = produceState(current, this, context) {
    if (context == EmptyCoroutineContext)
    {
        collect { value = it }
    } else withContext(context) {
        collect { value = it }
    }
}

