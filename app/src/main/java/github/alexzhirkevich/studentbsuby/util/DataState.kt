package github.alexzhirkevich.studentbsuby.util

import androidx.annotation.StringRes

sealed interface DataState<out T>
{

    data object Empty : DataState<Nothing>

    data object Loading : DataState<Nothing>

    data class Success<out T>(val value: T) : DataState<T>

    data class Error(@StringRes val message: Int, val error: Throwable? = null) : DataState<Nothing>
}

fun <T> DataState<T>.valueOrNull() = (this as? DataState.Success)?.value