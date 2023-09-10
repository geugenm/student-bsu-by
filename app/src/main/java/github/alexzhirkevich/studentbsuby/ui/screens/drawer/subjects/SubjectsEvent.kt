package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import androidx.compose.ui.focus.FocusRequester
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.util.Event

sealed interface SubjectsEvent : Event
{
    class SelectedSemesterChanged(val value: Int) : SubjectsEvent
    object SubjectsWithExamFilterPressed : SubjectsEvent
    object SubjectsWithCreditFilterPressed : SubjectsEvent
    class SubjectsSearchChanged(val value: String) : SubjectsEvent
    class CancelSearchCliched(val focusRequester: FocusRequester) : SubjectsEvent
    object UpdateRequested : SubjectsEvent
    class SubjectClicked(val value: Subject) : SubjectsEvent
}