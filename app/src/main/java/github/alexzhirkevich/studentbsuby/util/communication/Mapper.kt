package github.alexzhirkevich.studentbsuby.util.communication

fun interface Mapper<T>
{
    fun map(data: T)
}