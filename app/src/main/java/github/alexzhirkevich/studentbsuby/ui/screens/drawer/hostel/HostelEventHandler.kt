package github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel

import android.content.Context
import android.content.Intent
import android.net.Uri
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.HostelRepository
import github.alexzhirkevich.studentbsuby.repo.HostelState
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty

class HostelEventHandler(
    context: Context,
    hostelRepository: HostelRepository,
    connectivityManager: ConnectivityManager,
    isUpdatingMapper: Mapper<Boolean>,
    hostelStateMapper: StateMapper<DataState<HostelState>>
                        ) : SuspendEventHandler<HostelEvent> by SuspendEventHandler.from(
    UpdateRequestedHandler(
        hostelRepository = hostelRepository,
        connectivityManager = connectivityManager,
        isUpdatingMapper = isUpdatingMapper,
        hostelStateMapper = hostelStateMapper
                          ),
    ShowHostelOnMapClickedHandler(context, hostelRepository),
    ShowAdOnMapClickedHandler(context, hostelRepository),
    CallAdClickedHandler(context)
                                                                                        )

private class UpdateRequestedHandler(
    private val hostelRepository: HostelRepository,
    private val connectivityManager: ConnectivityManager,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val hostelStateMapper: StateMapper<DataState<HostelState>>
                                    ) : BaseSuspendEventHandler<HostelEvent.UpdateRequested>(
    HostelEvent.UpdateRequested::class
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

    override suspend fun handle(event: HostelEvent.UpdateRequested)
    {
        isUpdatingMapper.map(true)
        update(DataSource.Remote)
        isUpdatingMapper.map(false)
    }

    private suspend fun update(dataSource: DataSource)
    {
        hostelRepository.get(dataSource).onEach {
                hostelStateMapper.map(DataState.Success(it))
            }.onEmpty {
                hostelStateMapper.map(DataState.Empty)
            }.catch {
                if (hostelStateMapper.current !is DataState.Success)
                {
                    hostelStateMapper.map(
                        DataState.Error(
                            R.string.error_load_hostel, it
                                       )
                                         )
                }
            }.collect()
    }
}

private class ShowHostelOnMapClickedHandler(
    private val context: Context,
    private val hostelRepository: HostelRepository,
                                           ) :
    BaseSuspendEventHandler<HostelEvent.ShowHostelOnMapClicked>(
        HostelEvent.ShowHostelOnMapClicked::class
                                                               )
{
    override suspend fun handle(event: HostelEvent.ShowHostelOnMapClicked)
    {
        showOnMap(context, hostelRepository.getMapAddress(event.hostel.address))
    }
}

private class ShowAdOnMapClickedHandler(
    private val context: Context,
    private val hostelRepository: HostelRepository,
                                       ) : BaseSuspendEventHandler<HostelEvent.ShowAdOnMapClicked>(
    HostelEvent.ShowAdOnMapClicked::class
                                                                                                  )
{

    override suspend fun handle(event: HostelEvent.ShowAdOnMapClicked)
    {
        event.ad.address?.let {
            showOnMap(context, hostelRepository.getMapAddress(it))
        }
    }
}

private class CallAdClickedHandler(
    private val context: Context
                                  ) : BaseSuspendEventHandler<HostelEvent.CallClicked>(
    HostelEvent.CallClicked::class
                                                                                      )
{
    override suspend fun handle(event: HostelEvent.CallClicked)
    {
        event.ad.phone?.let {
            val phone = it.filter {
                    it == '+' || it.isDigit()
                }
            val intent = Intent(
                Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)
                               ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}

private fun showOnMap(context: Context, address: String)
{
    val uri = Uri.parse("geo:0,0?q=$address")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    context.startActivity(intent)
}