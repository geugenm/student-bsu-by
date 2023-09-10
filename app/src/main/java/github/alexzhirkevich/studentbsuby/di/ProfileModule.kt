package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.data.models.User
import github.alexzhirkevich.studentbsuby.repo.LoginRepository
import github.alexzhirkevich.studentbsuby.repo.PhotoRepository
import github.alexzhirkevich.studentbsuby.repo.UserRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.*
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.*
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule
{

    private val imageCommunication = StateFlowCommunication<DataState<ImageBitmap>>(
        DataState.Loading
                                                                                   )
    private val userCommunication = StateFlowCommunication<DataState<User>>(
        DataState.Loading
                                                                           )

    private val routeCommunication = StateFlowCommunication<DrawerRoute>(
        DrawerRoute.Timetable
                                                                        )

    @Provides
    fun provideImageCommunication(): StateCommunication<DataState<ImageBitmap>> = imageCommunication

    @Provides
    fun provideUserCommunication(): StateCommunication<DataState<User>> = userCommunication


    @Provides
    fun provideConnectivityCommunication(
        @ApplicationContext context: Context,
                                        ): Communication<ConnectivityUi> =
        BroadcastReceiverCommunication(
            context, ConnectivityUi::class.java.simpleName, ConnectivityUiSerializer
                                      )

    @Provides
    fun provideEventHandler(
        dispatchers: Dispatchers,
        connectivityManager: ConnectivityManager,
        loginRepository: LoginRepository,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        connectivityMapper: BroadcastMapper<ConnectivityUi>
                           ): SuspendEventHandler<ProfileEvent> = ProfileEventHandler(
        dispatchers = dispatchers,
        connectivityManager = connectivityManager,
        loginRepository = loginRepository,
        userRepository = userRepository,
        photoRepository = photoRepository,
        routeMapper = routeCommunication,
        connectivityMapper = connectivityMapper,
        imageMapper = imageCommunication,
        userMapper = userCommunication
                                                                                     )


}