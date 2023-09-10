package github.alexzhirkevich.studentbsuby.util.dispatchers

fun interface CoroutineJobCancel
{
    fun cancel(key: Any?)
}
