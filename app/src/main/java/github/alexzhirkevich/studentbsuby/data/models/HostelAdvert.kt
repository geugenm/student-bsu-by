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
{}