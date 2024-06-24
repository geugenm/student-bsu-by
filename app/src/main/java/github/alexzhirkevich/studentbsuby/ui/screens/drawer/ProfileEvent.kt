package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.navigation.Route
import github.alexzhirkevich.studentbsuby.util.Event
import github.alexzhirkevich.studentbsuby.util.communication.Serializer

sealed class DrawerRoute(
    val icon: ImageVector, @StringRes val title: Int, val route: Route
                        )
{
    data object Subjects :
        DrawerRoute(Icons.Default.Dashboard, R.string.subjects, Route.DrawerScreen.Subjects)

    data object Timetable : DrawerRoute(
        Icons.AutoMirrored.Filled.FormatListBulleted,
        R.string.timetable,
        Route.DrawerScreen.Timetable
                                       )

    data object About : DrawerRoute(Icons.Default.Info, R.string.about, Route.DrawerScreen.About)
    data object Hostel : DrawerRoute(Icons.Default.House, R.string.hostel, Route.DrawerScreen.Hostel)
    data object PaidServices :
        DrawerRoute(Icons.Default.Payment, R.string.paidservices, Route.DrawerScreen.PaidServices)

    data object News : DrawerRoute(Icons.Default.Campaign, R.string.news, Route.DrawerScreen.News)
}

enum class ConnectivityUi
{
    Connected,
    Connecting,
    Offline,

}

object ConnectivityUiSerializer : Serializer<ConnectivityUi>
{
    override fun serialize(value: ConnectivityUi): Bundle
    {
        return bundleOf("key" to value.name)
    }

    override fun deserialize(bundle: Bundle): ConnectivityUi
    {
        val name = bundle.getString("key")!!
        return ConnectivityUi.valueOf(name)
    }
}

sealed interface ProfileEvent : Event
{

    data object UpdateRequested : ProfileEvent
    class Logout(val navController: NavController) : ProfileEvent
    class RouteSelected(
        val route: DrawerRoute, val navController: NavController
                       ) : ProfileEvent

    class SettingsClicked(val navController: NavController) : ProfileEvent
}