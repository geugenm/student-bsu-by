package github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel

import github.alexzhirkevich.studentbsuby.data.models.HostelAdvert
import github.alexzhirkevich.studentbsuby.repo.HostelState
import github.alexzhirkevich.studentbsuby.util.Event

sealed interface HostelEvent : Event
{
    object UpdateRequested : HostelEvent
    class ShowHostelOnMapClicked(val hostel: HostelState.Provided) : HostelEvent
    class ShowAdOnMapClicked(val ad: HostelAdvert) : HostelEvent
    class CallClicked(val ad: HostelAdvert) : HostelEvent
}