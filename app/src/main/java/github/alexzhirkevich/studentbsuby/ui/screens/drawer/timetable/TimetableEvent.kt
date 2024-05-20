package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import github.alexzhirkevich.studentbsuby.data.models.Lesson
import github.alexzhirkevich.studentbsuby.util.Event

enum class LessonState
{
    INCOMING,
    RUNNING,
    PASSED
}

typealias Timetable = List<List<Pair<Lesson, LessonState>>>

sealed interface TimetableEvent : Event
{
    data object UpdateRequested : TimetableEvent
}