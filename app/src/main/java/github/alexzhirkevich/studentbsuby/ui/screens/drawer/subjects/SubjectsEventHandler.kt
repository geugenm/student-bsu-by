package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.repo.CurrentSemesterRepository
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.SubjectsRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateHolder
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty

class SubjectsEventHandler(
    subjectRepository: SubjectsRepository,
    currentSemesterRepository: CurrentSemesterRepository,
    connectivityManager: ConnectivityManager,
    isUpdatingMapper: Mapper<Boolean>,
    semesterMapper: Mapper<Int>,
    searchMapper: StateMapper<String>,
    withExamMapper: StateMapper<Boolean>,
    withCreditMapper: StateMapper<Boolean>,
    subjectsMapper: StateMapper<DataState<List<List<Subject>>>>,
    visibleSubjectsMapper: StateMapper<DataState<List<List<Subject>>>>,
                          ) : BaseSuspendEventHandler<SubjectsEvent>(
    SubjectsEvent::class
                                                                    )
{

    private val searchChangedHandler: SuspendEventHandler<SubjectsEvent.SubjectsSearchChanged> =
        SubjectsSearchChangedHandler(
            searchMapper, subjectsMapper, visibleSubjectsMapper, withCreditMapper, withExamMapper
                                    )

    private val handler = SuspendEventHandler.from(
        searchChangedHandler,
        SelectedSemesterChangedHandler(
            currentSemesterRepository, semesterMapper
                                      ),
        SubjectsWithExamFilterPressedHandler(
            withExamMapper, searchMapper, searchChangedHandler
                                            ),
        SubjectsWithCreditFilterPressedHandler(
            withCreditMapper, searchMapper, searchChangedHandler
                                              ),
        CancelSearchClichedHandler(searchChangedHandler),
        UpdateRequestedHandler(
            subjectsRepository = subjectRepository,
            connectivityManager = connectivityManager,
            isUpdatingMapper = isUpdatingMapper,
            subjectsMapper = subjectsMapper,
            searchState = searchMapper,
            searchChangedHandler = searchChangedHandler
                              ),
        SubjectClickedHandler(emptyList())
                                                  )

    override suspend fun handle(event: SubjectsEvent) = handler.handle(event)

    override fun release() = handler.release()

    override suspend fun launch() = handler.launch()
}

private class SelectedSemesterChangedHandler(
    private val currentSemesterRepository: CurrentSemesterRepository,
    private val semesterMapper: Mapper<Int>,
                                            ) :
    BaseSuspendEventHandler<SubjectsEvent.SelectedSemesterChanged>(
        SubjectsEvent.SelectedSemesterChanged::class
                                                                  )
{

    private var changedByUser = false

    override suspend fun launch()
    {
        currentSemesterRepository.get().onEach {
            if (!changedByUser) semesterMapper.map(it)
        }.catch {

        }.collect()
    }

    override suspend fun handle(event: SubjectsEvent.SelectedSemesterChanged)
    {
        changedByUser = true
        semesterMapper.map(event.value)
    }
}

private class SubjectsWithExamFilterPressedHandler(
    private val withExamMapper: StateMapper<Boolean>,
    private val searchState: StateHolder<String>,
    private val searchChangedHandler: SuspendEventHandler<SubjectsEvent.SubjectsSearchChanged>,
                                                  ) :
    BaseSuspendEventHandler<SubjectsEvent.SubjectsWithExamFilterPressed>(
        SubjectsEvent.SubjectsWithExamFilterPressed::class
                                                                        )
{
    override suspend fun handle(event: SubjectsEvent.SubjectsWithExamFilterPressed)
    {
        withExamMapper.map(withExamMapper.current.not())
        searchChangedHandler.handle(
            SubjectsEvent.SubjectsSearchChanged(
                searchState.current
                                               )
                                   )
    }
}

private class SubjectsWithCreditFilterPressedHandler(
    private val withCreditMapper: StateMapper<Boolean>,
    private val searchState: StateHolder<String>,
    private val searchChangedHandler: SuspendEventHandler<SubjectsEvent.SubjectsSearchChanged>,
                                                    ) :
    BaseSuspendEventHandler<SubjectsEvent.SubjectsWithCreditFilterPressed>(
        SubjectsEvent.SubjectsWithCreditFilterPressed::class
                                                                          )
{
    override suspend fun handle(event: SubjectsEvent.SubjectsWithCreditFilterPressed)
    {
        withCreditMapper.map(withCreditMapper.current.not())
        searchChangedHandler.handle(
            SubjectsEvent.SubjectsSearchChanged(
                searchState.current
                                               )
                                   )
    }
}

private class SubjectsSearchChangedHandler(
    private val searchMapper: Mapper<String>,
    private val subjectsState: StateHolder<DataState<List<List<Subject>>>>,
    private val visibleSubjectsMapper: Mapper<DataState<List<List<Subject>>>>,
    private val withCreditState: StateHolder<Boolean>,
    private val withExamState: StateHolder<Boolean>,
                                          ) :
    BaseSuspendEventHandler<SubjectsEvent.SubjectsSearchChanged>(
        SubjectsEvent.SubjectsSearchChanged::class
                                                                )
{
    override suspend fun handle(event: SubjectsEvent.SubjectsSearchChanged)
    {
        searchMapper.map(event.value)

        when (val subjects = subjectsState.current)
        {
            is DataState.Success ->
            {
                val newValue = subjects.value.map { list ->
                    list.filter {
                        (event.value.isEmpty() || it.name.contains(
                            event.value, true
                                                                  )) && (!withCreditState.current || it.hasCredit) && (!withExamState.current || it.hasExam)
                    }
                }
                visibleSubjectsMapper.map(DataState.Success(newValue))
            }

            else                 -> visibleSubjectsMapper.map(subjects)
        }
    }
}

private class CancelSearchClichedHandler(
    private val searchChangedHandler: SuspendEventHandler<SubjectsEvent.SubjectsSearchChanged>,
                                        ) :
    BaseSuspendEventHandler<SubjectsEvent.CancelSearchCliched>(
        SubjectsEvent.CancelSearchCliched::class
                                                              )
{
    override suspend fun handle(event: SubjectsEvent.CancelSearchCliched)
    {
        event.focusRequester.freeFocus()
        searchChangedHandler.handle(SubjectsEvent.SubjectsSearchChanged(""))
    }
}

private class UpdateRequestedHandler(
    private val subjectsRepository: SubjectsRepository,
    private val connectivityManager: ConnectivityManager,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val subjectsMapper: StateMapper<DataState<List<List<Subject>>>>,
    private val searchState: StateHolder<String>,
    private val searchChangedHandler: SuspendEventHandler<SubjectsEvent.SubjectsSearchChanged>,
                                    ) : BaseSuspendEventHandler<SubjectsEvent.UpdateRequested>(
    SubjectsEvent.UpdateRequested::class
                                                                                              )
{

    override suspend fun launch()
    {
        isUpdatingMapper.map(false)
        update(DataSource.All)
        connectivityManager.isNetworkConnected.collect {
            if (it)
            {
                update(DataSource.Remote)
            }
        }
    }

    override suspend fun handle(event: SubjectsEvent.UpdateRequested)
    {
        isUpdatingMapper.map(true)
        update(DataSource.Remote)
        isUpdatingMapper.map(false)
    }

    private suspend fun update(dataSource: DataSource)
    {
        subjectsRepository.get(dataSource).onEach {
            subjectsMapper.map(DataState.Success(it))
            searchChangedHandler.handle(
                SubjectsEvent.SubjectsSearchChanged(searchState.current)
                                       )
        }.onEmpty {
            subjectsMapper.map(DataState.Empty)
            searchChangedHandler.handle(
                SubjectsEvent.SubjectsSearchChanged(searchState.current)
                                       )
        }.catch {
            if (subjectsMapper.current !is DataState.Success)
            {
                subjectsMapper.map(
                    DataState.Error(
                        R.string.error_load_subjects, it
                                   )
                                  )
            }
        }.collect()
    }
}

private class SubjectClickedHandler(
    private val openedSubjects: List<List<Subject>>
                                   ) : BaseSuspendEventHandler<SubjectsEvent.SubjectClicked>(
    SubjectsEvent.SubjectClicked::class
                                                                                            )
{
    override suspend fun handle(event: SubjectsEvent.SubjectClicked)
    {

    }
}