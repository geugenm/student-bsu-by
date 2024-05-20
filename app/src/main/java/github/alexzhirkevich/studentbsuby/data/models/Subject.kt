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
}