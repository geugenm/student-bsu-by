package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Lesson
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.TimetableRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.Calendar
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TimetableEventHandler(
    private val timetableRepository: TimetableRepository,
    private val connectivityManager: ConnectivityManager,
    private val calendar: Calendar,
    private val timetableMapper: StateMapper<DataState<Timetable>>,
    private val isUpdatingMapper: Mapper<Boolean>
                           ) : SuspendEventHandler<TimetableEvent> by SuspendEventHandler.from(
    UpdateRequestedHandler(
        timetableRepository = timetableRepository,
        connectivityManager = connectivityManager,
        calendar = calendar,
        isUpdatingMapper = isUpdatingMapper,
        timetableMapper = timetableMapper
                          )
                                                                                              )

private class UpdateRequestedHandler(
    private val timetableRepository: TimetableRepository,
    private val connectivityManager: ConnectivityManager,
    private val calendar: Calendar,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val timetableMapper: StateMapper<DataState<Timetable>>,
                                    ) : BaseSuspendEventHandler<TimetableEvent.UpdateRequested>(
    TimetableEvent.UpdateRequested::class
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

    override suspend fun handle(event: TimetableEvent.UpdateRequested)
    {
        isUpdatingMapper.map(true)
        update(DataSource.Remote)
        isUpdatingMapper.map(false)
    }

    private suspend fun update(dataSource: DataSource) {
        if (dataSource == DataSource.Remote || dataSource == DataSource.All) {
            timetableRepository.init()
        }

        timetableRepository.get(dataSource)
            .map { timetable ->
                if (timetable.any { it.isNotEmpty() }) {
                    DataState.Success(timetable.map { day ->
                        day.mapIndexed { _, lesson ->
                            lesson to getLessonState(lesson)
                        }
                    })
                } else {
                    DataState.Empty
                }
            }
            .catch {
                if (timetableMapper.current !is DataState.Success) {
                    timetableMapper.map(DataState.Error(R.string.error_load_timetable, it))
                }
            }
            .collect { state ->
                timetableMapper.map(state)
            }
    }

    private fun getLessonState(lesson: Lesson): LessonState {
        val time = calendar.time()
        return when {
            lesson.dayOfWeek < calendar.dayOfWeek || (lesson.dayOfWeek == calendar.dayOfWeek && lesson.ends <= time) -> LessonState.PASSED
            lesson.dayOfWeek == calendar.dayOfWeek && lesson.ends > time -> LessonState.RUNNING
            else -> LessonState.INCOMING
        }
    }}