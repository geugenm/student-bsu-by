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

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as TuitionFeePayment

        if (year != other.year) return false
        if (fullPrice != other.fullPrice) return false
        if (fineDays != other.fineDays) return false
        if (fineSize != other.fineSize) return false
        if (debt != other.debt) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = super.hashCode()
        result = 31 * result + year.hashCode()
        result = 31 * result + fullPrice.hashCode()
        result = 31 * result + fineDays
        result = 31 * result + fineSize.hashCode()
        result = 31 * result + (debt?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String
    {
        return "TuitionFeeReceipt(id=$id, year='$year', fullPrice=$fullPrice, fineDays=$fineDays, fineSize=$fineSize, debt=$debt)"
    }
}