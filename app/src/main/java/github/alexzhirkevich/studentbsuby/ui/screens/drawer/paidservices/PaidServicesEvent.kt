package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import github.alexzhirkevich.studentbsuby.util.Event

sealed interface PaidServicesEvent : Event
{
    object UpdateRequested : PaidServicesEvent
    object EripHelpClicked : PaidServicesEvent
}