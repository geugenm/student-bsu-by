package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.repo.HostelRepository
import github.alexzhirkevich.studentbsuby.repo.HostelState
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel.HostelEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel.HostelEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import javax.inject.Qualifier

@Qualifier
annotation class IsHostelUpdatingQualifier

@Module
@InstallIn(ViewModelComponent::class)
class HostelModule
{

    private val isUpdatingCommunication = StateFlowCommunication(false)
    private val hostelStateCommunication =
        StateFlowCommunication<DataState<HostelState>>(DataState.Loading)

    @IsHostelUpdatingQualifier
    @Provides
    fun provideIsUpdatingCommunication(): StateCommunication<Boolean> = isUpdatingCommunication

    @Provides
    fun provideHostelStateCommunication(): StateCommunication<DataState<HostelState>> =
        hostelStateCommunication

    @Provides
    fun provideEventHandler(
        @ApplicationContext context: Context,
        hostelRepository: HostelRepository,
        connectivityManager: ConnectivityManager,
                           ): SuspendEventHandler<HostelEvent> = HostelEventHandler(
        context = context,
        hostelRepository = hostelRepository,
        isUpdatingMapper = isUpdatingCommunication,
        hostelStateMapper = hostelStateCommunication,
        connectivityManager = connectivityManager,
                                                                                   )
}