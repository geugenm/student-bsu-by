package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HostelAdvert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val number: Int,
    val phone: String?,
    val publisher: String?,
    val address: String?,
    val conditions: String?,
    val price: String?,
    val currency: String?,
    @ColumnInfo(name = "publish_date") val publishDate: String,
    val note: String?,
                       )
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HostelAdvert

        if (number != other.number) return false
        if (phone != other.phone) return false
        if (publisher != other.publisher) return false
        if (address != other.address) return false
        if (conditions != other.conditions) return false
        if (price != other.price) return false
        if (currency != other.currency) return false
        if (publishDate != other.publishDate) return false
        if (note != other.note) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = number
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (publisher?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (conditions?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (currency?.hashCode() ?: 0)
        result = 31 * result + publishDate.hashCode()
        result = 31 * result + (note?.hashCode() ?: 0)
        return result
    }
}