package github.alexzhirkevich.studentbsuby

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.di.ShowUpdateQualifier
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ShowUpdateQualifier val showUpdateDialog: StateCommunication<Boolean>,
    dispatchers: Dispatchers,
    errorHandler: ErrorHandler,
    eventHandler: SuspendEventHandler<MainActivityEvent>
                                               ) : SuspendHandlerViewModel<MainActivityEvent>(
    dispatchers = dispatchers, suspendEventHandler = eventHandler, errorHandler = errorHandler
                                                                                             )