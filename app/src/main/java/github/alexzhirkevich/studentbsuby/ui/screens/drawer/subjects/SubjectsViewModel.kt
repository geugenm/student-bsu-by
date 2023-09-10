package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.di.CreditQualifier
import github.alexzhirkevich.studentbsuby.di.ExamQualifier
import github.alexzhirkevich.studentbsuby.di.IsSubjectsUpdatingQualifier
import github.alexzhirkevich.studentbsuby.di.SearchQualifier
import github.alexzhirkevich.studentbsuby.di.SemesterQualifier
import github.alexzhirkevich.studentbsuby.di.SubjectsQualifier
import github.alexzhirkevich.studentbsuby.di.VisibleSubjectsQualifier
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.Updatable
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SubjectsViewModel @Inject constructor(
    @IsSubjectsUpdatingQualifier override val isUpdating: StateCommunication<Boolean>,
    @SemesterQualifier val semesterCommunication: StateCommunication<Int>,
    @SearchQualifier val searchCommunication: StateCommunication<String>,
    @ExamQualifier val withExamCommunication: StateCommunication<Boolean>,
    @CreditQualifier val withCreditCommunication: StateCommunication<Boolean>,
    @SubjectsQualifier
    val subjectsCommunication: StateCommunication<DataState<List<List<Subject>>>>,
    @VisibleSubjectsQualifier
    val visibleSubjectsCommunication: StateCommunication<DataState<List<List<Subject>>>>,
    eventHandler: SuspendEventHandler<SubjectsEvent>,
    errorHandler: ErrorHandler,
    dispatchers: Dispatchers
                                           ) : SuspendHandlerViewModel<SubjectsEvent>(
    suspendEventHandler = eventHandler, errorHandler = errorHandler, dispatchers = dispatchers
                                                                                     ), Updatable
{

    override fun update()
    {
        handle(SubjectsEvent.UpdateRequested)
    }
}