package github.alexzhirkevich.studentbsuby.util

import java.util.Calendar as JCalendar

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface Calendar {
    val dayOfMonth: Int
    val dayOfWeek: Int
    val month: Int
    val year: Int
    fun time(): String
}

object CurrentCalendar : Calendar {
    private val now = LocalDateTime.now()
    override val dayOfMonth: Int = now.dayOfMonth
    override val dayOfWeek: Int = now.dayOfWeek.value % 7 // Adjust for 0-based indexing
    override val month: Int = now.monthValue
    override val year: Int = now.year

    override fun time(): String = now.format(DateTimeFormatter.ofPattern("HH:mm"))
}