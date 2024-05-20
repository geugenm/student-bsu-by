package github.alexzhirkevich.studentbsuby.data.models

import androidx.annotation.IntRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lesson(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    @IntRange(from = 0, to = 5) @ColumnInfo(name = "day_of_week") val dayOfWeek: Int,
    val number: Int,
    val name: String,
    val place: String,
    val type: String,
    val teacher: String,
    val starts: String,
    val ends: String
                 )
{
}