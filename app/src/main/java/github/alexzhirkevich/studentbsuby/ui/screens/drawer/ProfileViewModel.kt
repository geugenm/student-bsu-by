package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.data.models.User
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.communication.Communication
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.ExperimentalToolbarApi
import javax.inject.Inject


@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    val connectivityCommunication: Communication<ConnectivityUi>,
    val userCommunication: StateCommunication<DataState<User>>,
    val imageCommunication: StateCommunication<DataState<ImageBitmap>>,
    dispatchers: Dispatchers,
    errorHandler: ErrorHandler,
    eventHandler: SuspendEventHandler<ProfileEvent>
                                          ) : SuspendHandlerViewModel<ProfileEvent>(
    dispatchers = dispatchers, errorHandler = errorHandler, suspendEventHandler = eventHandler
                                                                                   )