package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavGraph.Companion.findStartDestination
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.User
import github.alexzhirkevich.studentbsuby.navigation.Route
import github.alexzhirkevich.studentbsuby.navigation.navigate
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.LoginRepository
import github.alexzhirkevich.studentbsuby.repo.PhotoRepository
import github.alexzhirkevich.studentbsuby.repo.UserRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class ProfileEventHandler(
    private val dispatchers: Dispatchers,
    private val connectivityManager: ConnectivityManager,
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val routeMapper: StateMapper<DrawerRoute>,
    private val connectivityMapper: Mapper<ConnectivityUi>,
    private val imageMapper: StateMapper<DataState<ImageBitmap>>,
    private val userMapper: StateMapper<DataState<User>>
                         ) : SuspendEventHandler<ProfileEvent> by SuspendEventHandler.from(
    ProfileEvent::class,
    LogoutEventHandler(dispatchers, loginRepository),
    RouteSelectedHandler(routeMapper, dispatchers),
    SettingClickedHandler(),
    UpdateRequestedHandler(
        connectivityManager = connectivityManager,
        userRepository = userRepository,
        photoRepository = photoRepository,
        loginRepository = loginRepository,
        userMapper = userMapper,
        imageMapper = imageMapper,
        connectivityMapper = connectivityMapper,
                          )
                                                                                          )

private class RouteSelectedHandler(
    private val routeMapper: StateMapper<DrawerRoute>, private val dispatchers: Dispatchers
                                  ) : BaseSuspendEventHandler<ProfileEvent.RouteSelected>(
    ProfileEvent.RouteSelected::class
                                                                                         )
{
    override suspend fun handle(event: ProfileEvent.RouteSelected)
    {
        dispatchers.runOnUI {
            if (routeMapper.current != event.route.route)
            {
                routeMapper.map(event.route)
                event.navController.navigate(event.route.route) {
                    val last = event.navController.backQueue.lastOrNull()?.destination?.id
                        ?: event.navController.graph.findStartDestination().id
                    popUpTo(last) {
                        event.navController.backQueue.last().destination
                        saveState = true
                        inclusive = true
                    }

                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}

private class SettingClickedHandler : BaseSuspendEventHandler<ProfileEvent.SettingsClicked>(
    ProfileEvent.SettingsClicked::class
                                                                                           )
{
    override suspend fun handle(event: ProfileEvent.SettingsClicked)
    {
        event.navController.navigate(Route.SettingsScreen)
    }
}

private class LogoutEventHandler(
    private val dispatchers: Dispatchers,
    private val loginRepository: LoginRepository,
                                ) : BaseSuspendEventHandler<ProfileEvent.Logout>(
    ProfileEvent.Logout::class
                                                                                )
{
    override suspend fun handle(event: ProfileEvent.Logout)
    {
        loginRepository.logout()
        dispatchers.runOnUI {
            event.navController.popBackStack()
            event.navController.navigate(Route.AuthScreen)
        }
    }
}

private class UpdateRequestedHandler(
    private val connectivityManager: ConnectivityManager,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val loginRepository: LoginRepository,
    private val userMapper: StateMapper<DataState<User>>,
    private val imageMapper: StateMapper<DataState<ImageBitmap>>,
    private val connectivityMapper: Mapper<ConnectivityUi>,
                                    ) : BaseSuspendEventHandler<ProfileEvent.UpdateRequested>(
    ProfileEvent.UpdateRequested::class
                                                                                             )
{

    override suspend fun launch()
    {
        userMapper.map(DataState.Loading)
        imageMapper.map(DataState.Loading)
        update(DataSource.All)
        connectivityMapper.map(ConnectivityUi.Connected)

        var values = 0L

        connectivityManager.isNetworkConnected.collect {

            val logged = if (values < 2) true else kotlin.runCatching {
                loginRepository.initialize().loggedIn
            }.getOrDefault(false)

            values++

            connectivityMapper.map(
                when
                {
                    !it     -> ConnectivityUi.Connecting
                    !logged -> ConnectivityUi.Offline
                    else    -> ConnectivityUi.Connected
                }
                                  )

            if (it)
            {
                update(DataSource.Remote)
            }
        }
    }

    override suspend fun handle(event: ProfileEvent.UpdateRequested)
    {
        update(DataSource.Remote)
    }

    private suspend fun update(source: DataSource) = coroutineScope {
        launch {
            photoRepository.get(source).onEach {
                    imageMapper.map(DataState.Success(it.asImageBitmap()))
                }.onEmpty {
                    imageMapper.map(DataState.Empty)
                }.catch {
                    if (imageMapper.current !is DataState.Success)
                    {
                        imageMapper.map(
                            DataState.Error(
                                R.string.error_load_photo, it
                                           )
                                       )
                    }
                }.collect()
        }
        launch {
            userRepository.get(source).onEach {
                    userMapper.map(DataState.Success(it))
                }.onEmpty {
                    userMapper.map(DataState.Empty)
                }.catch {
                    if (userMapper.current !is DataState.Success)
                    {
                        userMapper.map(
                            DataState.Error(
                                R.string.error_load_user, it
                                           )
                                      )
                    }
                }.collect()
        }
    }
}