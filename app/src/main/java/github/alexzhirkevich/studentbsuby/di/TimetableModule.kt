package github.alexzhirkevich.studentbsuby.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.alexzhirkevich.studentbsuby.repo.TimetableRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable.Timetable
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable.TimetableEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable.TimetableEventHandler
import github.alexzhirkevich.studentbsuby.util.Calendar
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import javax.inject.Qualifier

@Qualifier
annotation class IsTimetableUpdatingQualifier

@Module
@InstallIn(ViewModelComponent::class)
class TimetableModule
{

    private val isUpdatingCommunication = StateFlowCommunication(false)

    private val timetableCommunication =
        StateFlowCommunication<DataState<Timetable>>(DataState.Empty)

    @Provides
    @IsTimetableUpdatingQualifier
    fun provideIsUpdatingCommunication(): StateCommunication<Boolean> = isUpdatingCommunication

    @Provides
    fun provideTimetableCommunication(): StateCommunication<DataState<Timetable>> =
        timetableCommunication

    @Provides
    fun provideEventHandler(
        calendar: Calendar,
        timetableRepository: TimetableRepository,
        connectivityManager: ConnectivityManager
                           ): SuspendEventHandler<TimetableEvent> = TimetableEventHandler(
        timetableRepository = timetableRepository,
        calendar = calendar,
        timetableMapper = timetableCommunication,
        isUpdatingMapper = isUpdatingCommunication,
        connectivityManager = connectivityManager
                                                                                         )
}