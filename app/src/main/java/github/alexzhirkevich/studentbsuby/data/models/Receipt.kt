package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Receipt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val deadline: Long,
    val date: Long?,
    val price: Float?,
    val left: Float?,
    val info: String?,
    val receiptType: Int
                  )
{

    companion object
    {
        const val TYPE_COMMON = 0
        const val TYPE_ACADEM_DEBT = 1
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Receipt

        if (owner != other.owner) return false
        if (deadline != other.deadline) return false
        if (date != other.date) return false
        if (price != other.price) return false
        if (left != other.left) return false
        if (info != other.info) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = owner.hashCode()
        result = 31 * result + deadline.hashCode()
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (left?.hashCode() ?: 0)
        result = 31 * result + (info?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String
    {
        return "Receipt(id=$id, owner='$owner', deadline=$deadline, date=$date, price=$price, left=$left, info=$info)"
    }

}