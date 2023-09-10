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
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Lesson

        if (owner != other.owner) return false
        if (dayOfWeek != other.dayOfWeek) return false
        if (number != other.number) return false
        if (name != other.name) return false
        if (place != other.place) return false
        if (type != other.type) return false
        if (teacher != other.teacher) return false
        if (starts != other.starts) return false
        if (ends != other.ends) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = owner.hashCode()
        result = 31 * result + dayOfWeek
        result = 31 * result + number
        result = 31 * result + name.hashCode()
        result = 31 * result + place.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + teacher.hashCode()
        result = 31 * result + starts.hashCode()
        result = 31 * result + ends.hashCode()
        return result
    }
}