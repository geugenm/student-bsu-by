package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val name: String,
    val lectures: Int,
    val practice: Int,
    val labs: Int,
    val seminars: Int,
    val facults: Int,
    val ksr: Int,
    @ColumnInfo(name = "has_credit") val hasCredit: Boolean,
    @ColumnInfo(name = "credit_passed") val creditPassed: Boolean?,
    @ColumnInfo(name = "credit_mark") val creditMark: Int?,
    @ColumnInfo(name = "credit_retakes") val creditRetakes: Int,
    @ColumnInfo(name = "has_exam") val hasExam: Boolean,
    @ColumnInfo(name = "exam_mark") val examMark: Int?,
    @ColumnInfo(name = "exam_retakes") val examRetakes: Int,
    val semester: Int
                  )
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subject

        if (owner != other.owner) return false
        if (name != other.name) return false
        if (lectures != other.lectures) return false
        if (practice != other.practice) return false
        if (labs != other.labs) return false
        if (seminars != other.seminars) return false
        if (facults != other.facults) return false
        if (ksr != other.ksr) return false
        if (hasCredit != other.hasCredit) return false
        if (creditPassed != other.creditPassed) return false
        if (creditMark != other.creditMark) return false
        if (creditRetakes != other.creditRetakes) return false
        if (hasExam != other.hasExam) return false
        if (examMark != other.examMark) return false
        if (examRetakes != other.examRetakes) return false
        if (semester != other.semester) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = name.hashCode()
        result = 31 * result + lectures
        result = 31 * result + practice
        result = 31 * result + labs
        result = 31 * result + seminars
        result = 31 * result + facults
        result = 31 * result + ksr
        result = 31 * result + hasCredit.hashCode()
        result = 31 * result + (creditPassed?.hashCode() ?: 0)
        result = 31 * result + (creditMark ?: 0)
        result = 31 * result + creditRetakes
        result = 31 * result + hasExam.hashCode()
        result = 31 * result + (examMark ?: 0)
        result = 31 * result + examRetakes
        result = 31 * result + semester
        return result
    }
}