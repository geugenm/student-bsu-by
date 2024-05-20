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

    override fun toString(): String
    {
        return "Receipt(id=$id, owner='$owner', deadline=$deadline, date=$date, price=$price, left=$left, info=$info)"
    }

}