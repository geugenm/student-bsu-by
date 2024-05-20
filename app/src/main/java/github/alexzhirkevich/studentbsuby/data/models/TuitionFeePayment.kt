package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TuitionFeePayment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val deadline: Long,
    val date: Long?,
    val price: Float?,
    val left: Float?,
    @ColumnInfo(name = "fUll_price") val fullPrice: Float,
    val year: String,
    @ColumnInfo(name = "fine_days") val fineDays: Int,
    @ColumnInfo(name = "fine_size") val fineSize: Float,
    val debt: Float?
                       )
{

    override fun toString(): String
    {
        return "TuitionFeeReceipt(id=$id, year='$year', fullPrice=$fullPrice, fineDays=$fineDays, fineSize=$fineSize, debt=$debt)"
    }
}