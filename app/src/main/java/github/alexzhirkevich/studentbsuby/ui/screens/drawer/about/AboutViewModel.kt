package github.alexzhirkevich.studentbsuby.ui.screens.drawer.about

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    errorHandler: ErrorHandler,
    dispatchers: Dispatchers,
    eventHandler: SuspendEventHandler<AboutEvent>
                                        ) : SuspendHandlerViewModel<AboutEvent>(
    errorHandler = errorHandler, dispatchers = dispatchers, suspendEventHandler = eventHandler
                                                                               )