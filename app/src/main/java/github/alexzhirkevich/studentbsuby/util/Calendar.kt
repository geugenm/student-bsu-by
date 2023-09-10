package github.alexzhirkevich.studentbsuby.util

import java.util.Calendar as JCalendar

interface Calendar
{
    val dayOfMonth: Int
    val dayOfWeek: Int
    val month: Int
    val year: Int
    fun time(): String

    class Base : Calendar
    {
        override val dayOfMonth = JCalendar.getInstance().get(JCalendar.DAY_OF_MONTH)

        override val dayOfWeek = when (JCalendar.getInstance().get(JCalendar.DAY_OF_WEEK))
        {
            JCalendar.MONDAY    -> 0
            JCalendar.TUESDAY   -> 1
            JCalendar.WEDNESDAY -> 2
            JCalendar.THURSDAY  -> 3
            JCalendar.FRIDAY    -> 4
            JCalendar.SATURDAY  -> 5
            else                -> 6
        }

        override val month = JCalendar.getInstance().get(JCalendar.MONTH)

        override val year = JCalendar.getInstance().get(JCalendar.YEAR)

        override fun time(): String = JCalendar.getInstance().let {
            "${it.get(JCalendar.HOUR_OF_DAY).let { if (it > 9) it else "0$it" }}:" + "${
                it.get(
                    JCalendar.MINUTE
                      ).let { if (it > 9) it else "0$it" }
            }"
        }
    }
}