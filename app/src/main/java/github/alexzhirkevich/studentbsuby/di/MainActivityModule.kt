package github.alexzhirkevich.studentbsuby.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.alexzhirkevich.studentbsuby.MainActivityEvent
import github.alexzhirkevich.studentbsuby.MainActivityEventHandler
import github.alexzhirkevich.studentbsuby.repo.RemoteConfigRepository
import github.alexzhirkevich.studentbsuby.repo.ReviewRepository
import github.alexzhirkevich.studentbsuby.repo.UpdateRepository
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import ru.mintrocket.lib.mintpermissions.MintPermissionsController
import javax.inject.Qualifier

@Qualifier
annotation class ShowUpdateQualifier

@Module
@InstallIn(ViewModelComponent::class)
class MainActivityModule
{

    private val showUpdate = StateFlowCommunication(false)

    @Provides
    @ShowUpdateQualifier
    fun provideUpdateCom(): StateCommunication<Boolean> = showUpdate

    @Provides
    fun provideEventHandler(
        dispatchers: Dispatchers,
        permissionsController: MintPermissionsController,
        remoteConfigRepository: RemoteConfigRepository,
        updateRepository: UpdateRepository,
        reviewRepository: ReviewRepository
                           ): SuspendEventHandler<MainActivityEvent> = MainActivityEventHandler(
        dispatchers = dispatchers,
        remoteConfigRepository = remoteConfigRepository,
        updateRepository = updateRepository,
        reviewRepository = reviewRepository,
        showUpdateRequired = showUpdate,
        mintPermissionsController = permissionsController
                                                                                               )
}