package github.alexzhirkevich.studentbsuby.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.repo.CurrentSemesterRepository
import github.alexzhirkevich.studentbsuby.repo.SubjectsRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects.SubjectsEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects.SubjectsEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import javax.inject.Qualifier

@Qualifier
annotation class IsSubjectsUpdatingQualifier

@Qualifier
annotation class SemesterQualifier

@Qualifier
annotation class SearchQualifier

@Qualifier
annotation class ExamQualifier

@Qualifier
annotation class CreditQualifier

@Qualifier
annotation class SubjectsQualifier

@Qualifier
annotation class VisibleSubjectsQualifier

@Module
@InstallIn(ViewModelComponent::class)
class SubjectsModule
{

    private val isUpdatingCommunication = StateFlowCommunication(false)
    private val semesterCommunication = StateFlowCommunication(0)
    private val searchCommunication = StateFlowCommunication("")
    private val withExamCommunication = StateFlowCommunication(false)
    private val withCreditCommunication = StateFlowCommunication(false)
    private val subjectsCommunication = StateFlowCommunication<DataState<List<List<Subject>>>>(
        DataState.Loading
                                                                                              )
    private val visibleSubjectsCommunication =
        StateFlowCommunication<DataState<List<List<Subject>>>>(
            DataState.Loading
                                                              )

    @Provides
    @IsSubjectsUpdatingQualifier
    fun provideIsUpdatingCommunication(): StateCommunication<Boolean> = isUpdatingCommunication

    @Provides
    @SemesterQualifier
    fun provideSemesterCommunication(): StateCommunication<Int> = semesterCommunication

    @Provides
    @SearchQualifier
    fun provideSearchCommunication(): StateCommunication<String> = searchCommunication

    @Provides
    @ExamQualifier
    fun provideExamCommunication(): StateCommunication<Boolean> = withExamCommunication


    @Provides
    @CreditQualifier
    fun provideCreditCommunication(): StateCommunication<Boolean> = withCreditCommunication


    @Provides
    @SubjectsQualifier
    fun provideSubjectsCommunication(): StateCommunication<DataState<List<List<Subject>>>> =
        subjectsCommunication

    @Provides
    @VisibleSubjectsQualifier
    fun provideVisibleSubjectsCommunication(): StateCommunication<DataState<List<List<Subject>>>> =
        visibleSubjectsCommunication

    @Provides
    fun provideEventHandler(
        subjectsRepository: SubjectsRepository,
        currentSemesterRepository: CurrentSemesterRepository,
        connectivityManager: ConnectivityManager,
                           ): SuspendEventHandler<SubjectsEvent> = SubjectsEventHandler(
        subjectRepository = subjectsRepository,
        currentSemesterRepository = currentSemesterRepository,
        connectivityManager = connectivityManager,
        isUpdatingMapper = isUpdatingCommunication,
        semesterMapper = semesterCommunication,
        searchMapper = searchCommunication,
        withExamMapper = withExamCommunication,
        withCreditMapper = withCreditCommunication,
        subjectsMapper = subjectsCommunication,
        visibleSubjectsMapper = visibleSubjectsCommunication
                                                                                       )
}