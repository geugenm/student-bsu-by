package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PaidServicesInfo(
    @PrimaryKey val owner: String,
    @ColumnInfo(name = "contract_number") val contractNumber: String,
    val debt: Float,
    val fine: Float,
    val price: Float? = null,
                           )

@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val paymentType: String,
    val deadline: Long,
    val price: Float,
    val billType: Int
               )
{
    companion object
    {
        const val TYPE_PAIDINFO = 0
        const val TYPE_HOSTEL = 1
        const val TYPE_COMMON = 2
    }
}