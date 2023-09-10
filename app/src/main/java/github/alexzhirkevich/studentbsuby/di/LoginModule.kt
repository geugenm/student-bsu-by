package github.alexzhirkevich.studentbsuby.di

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.alexzhirkevich.studentbsuby.repo.LoginRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.ConnectivityUi
import github.alexzhirkevich.studentbsuby.ui.screens.login.LoginEvent
import github.alexzhirkevich.studentbsuby.ui.screens.login.LoginEventHandler
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ResourceManager
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.*
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import github.alexzhirkevich.studentbsuby.workers.SyncWorkerManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.ExperimentalToolbarApi
import javax.inject.Qualifier

@Qualifier
annotation class AutoLoginEnabledQualifier

@Qualifier
annotation class ControlsEnabledQualifier

@Qualifier
annotation class LoginTextQualifier

@Qualifier
annotation class PassTextQualifier

@Qualifier
annotation class CaptchaTextQualifier

@Qualifier
annotation class CaptchaImageQualifier

@Qualifier
annotation class ErrorQualifier


@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Module
@InstallIn(ViewModelComponent::class)
class LoginModule
{

    private val login = StateFlowCommunication("")
    private val pass = StateFlowCommunication("")
    private val captcha = StateFlowCommunication("")
    private val captchaImage = StateFlowCommunication<DataState<ImageBitmap>>(DataState.Empty)
    private val autoLogin = StateFlowCommunication(false)
    private val error = SharedFlowCommunication<String>()
    private val controlsEnabled = StateFlowCommunication(true)

    @Provides
    @AutoLoginEnabledQualifier
    fun provideAutoLoginCommunication(): StateCommunication<Boolean> = autoLogin

    @Provides
    @ControlsEnabledQualifier
    fun provideControlsEnabledCommunication(): StateCommunication<Boolean> = controlsEnabled


    @Provides
    @LoginTextQualifier
    fun provideLoginTextCommunication(): StateCommunication<String> = login

    @Provides
    @PassTextQualifier
    fun providePassTextCommunication(): StateCommunication<String> = pass

    @Provides
    @CaptchaTextQualifier
    fun provideCaptchaTextCommunication(): StateCommunication<String> = captcha

    @Provides
    @CaptchaImageQualifier
    fun provideCaptchaImageCommunication(): StateCommunication<DataState<ImageBitmap>> =
        captchaImage

    @Provides
    @ErrorQualifier
    fun provideErrorCommunication(): Communication<String> = error


    @Provides
    fun provideEventHandler(
        dispatchers: Dispatchers,
        resourceManager: ResourceManager,
        loginRepository: LoginRepository,
        syncWorkerManager: SyncWorkerManager,
        connectivityMapper: BroadcastMapper<ConnectivityUi>
                           ): SuspendEventHandler<LoginEvent> = LoginEventHandler(
        dispatchers = dispatchers,
        resourceManager = resourceManager,
        loginRepository = loginRepository,
        loginMapper = login,
        passMapper = pass,
        captchaMapper = captcha,
        captchaImageMapper = captchaImage,
        autoLoginMapper = autoLogin,
        errorMapper = error,
        controlsEnabledMapper = controlsEnabled,
        syncWorkerManager = syncWorkerManager,
        connectivityMapper = connectivityMapper,
                                                                                 )
}