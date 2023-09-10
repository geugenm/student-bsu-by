package github.alexzhirkevich.studentbsuby.util.communication

import android.content.Context
import android.content.Intent

class BroadcastReceiverMapper<T>(
    private val context: Context, override val action: String, private val serializer: Serializer<T>

                                ) : BroadcastMapper<T>, Serializer<T> by serializer
{
    override fun map(data: T)
    {
        context.sendBroadcast(Intent(action).apply {
            putExtra(extraKey, serialize(data))
        })
    }
}