package github.alexzhirkevich.studentbsuby.ui.screens.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.di.AutoLoginEnabledQualifier
import github.alexzhirkevich.studentbsuby.di.CaptchaImageQualifier
import github.alexzhirkevich.studentbsuby.di.CaptchaTextQualifier
import github.alexzhirkevich.studentbsuby.di.ControlsEnabledQualifier
import github.alexzhirkevich.studentbsuby.di.ErrorQualifier
import github.alexzhirkevich.studentbsuby.di.LoginTextQualifier
import github.alexzhirkevich.studentbsuby.di.PassTextQualifier
import github.alexzhirkevich.studentbsuby.repo.LoginRepository
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.communication.Communication
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    eventHandler: SuspendEventHandler<LoginEvent>,
    dispatchers: Dispatchers,
    errorHandler: ErrorHandler,
    @LoginTextQualifier val login: StateCommunication<String>,
    @PassTextQualifier val password: StateCommunication<String>,
    @CaptchaTextQualifier val captcha: StateCommunication<String>,
    @CaptchaImageQualifier val captchaImage: StateCommunication<DataState<ImageBitmap>>,
    @AutoLoginEnabledQualifier val autoLogin: StateCommunication<Boolean>,
    @ControlsEnabledQualifier val controlsEnabled: StateCommunication<Boolean>,
    @ErrorQualifier val error: Communication<String>,
    loginRepository: LoginRepository
                                        ) : SuspendHandlerViewModel<LoginEvent>(
    dispatchers = dispatchers, suspendEventHandler = eventHandler, errorHandler = errorHandler
                                                                               )
{
    val skipLogin = loginRepository.autoLogin
}