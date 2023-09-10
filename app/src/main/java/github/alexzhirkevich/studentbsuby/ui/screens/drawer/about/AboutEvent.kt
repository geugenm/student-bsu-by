package github.alexzhirkevich.studentbsuby.ui.screens.drawer.about

import github.alexzhirkevich.studentbsuby.util.Event

sealed interface AboutEvent : Event
{
    object EmailClicked : AboutEvent
    object TgClicked : AboutEvent
}