package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import github.alexzhirkevich.studentbsuby.R
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty

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

    private suspend fun update(dataSource: DataSource)
    {
        if (dataSource == DataSource.Remote || dataSource == DataSource.All)
        {
            timetableRepository.init()
        }
        timetableRepository.get(dataSource).onEach {
                val state = if (it.any { it.isNotEmpty() }) DataState.Success(it.map { day ->
                    var hasRunning = false
                    val time = calendar.time()
                    day.mapIndexed { _, lesson ->
                        lesson to when
                        {
                            lesson.dayOfWeek < calendar.dayOfWeek || lesson.dayOfWeek == calendar.dayOfWeek && lesson.ends <= time -> LessonState.PASSED
                            !hasRunning && lesson.dayOfWeek == calendar.dayOfWeek && lesson.ends > time                            -> LessonState.RUNNING.also {
                                hasRunning = true
                            }

                            else                                                                                                   -> LessonState.INCOMING
                        }
                    }
                })
                else DataState.Empty

                timetableMapper.map(state)
            }.onEmpty {
                timetableMapper.map(DataState.Empty)
            }.catch {
                if (timetableMapper.current !is DataState.Success)
                {
                    timetableMapper.map(
                        DataState.Error(
                            R.string.error_load_timetable, it
                                       )
                                       )
                }
            }.collect()
    }
}