package github.alexzhirkevich.studentbsuby.util.communication

import android.os.Bundle
import androidx.core.os.bundleOf

interface Broadcast<T> : Serializer<T> {
    val action: String

    val extraKey: String get() = action + "_value"

    companion object
}

interface Serializer<T> {
    fun serialize(value : T): Bundle

    fun deserialize(bundle : Bundle) : T
}

@Suppress("UNCHECKED_CAST")
class PrimitiveSerializer<T> : Serializer<T> {
    override fun serialize(value: T): Bundle {
        return bundleOf("value" to value)
    }

    override fun deserialize(bundle: Bundle): T {
        return bundle.get("value") as T
    }

}