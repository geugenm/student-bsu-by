package github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.di.IsHostelUpdatingQualifier
import github.alexzhirkevich.studentbsuby.repo.HostelRepository
import github.alexzhirkevich.studentbsuby.repo.HostelState
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.Updatable
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HostelViewModel @Inject constructor(
    @IsHostelUpdatingQualifier override val isUpdating: StateCommunication<Boolean>,
    val hostelStateCommunication: StateCommunication<DataState<HostelState>>,
    private val hostelRepository: HostelRepository,
    dispatchers: Dispatchers,
    errorHandler: ErrorHandler,
    suspendEventHandler: SuspendEventHandler<HostelEvent>
                                         ) : SuspendHandlerViewModel<HostelEvent>(
    dispatchers = dispatchers,
    errorHandler = errorHandler,
    suspendEventHandler = suspendEventHandler
                                                                                 ), Updatable
{

    override fun update()
    {
        handle(HostelEvent.UpdateRequested)
    }

    fun getHostelImage(value: HostelState.Provided): String = hostelRepository.getImageForHostel(
        hostelRepository.getHostelNumber(value.address)
                                                                                                )
}