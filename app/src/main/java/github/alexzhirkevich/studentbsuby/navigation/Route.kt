package github.alexzhirkevich.studentbsuby.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val route: String)
{

    open val navArguments: List<NamedNavArgument> = emptyList()
    open fun getArguments(entry: NavBackStackEntry): Any? = null

    object AuthScreen : Route("auth")

    open class DrawerScreen private constructor(route: String = "drawer") : Route(route)
    {
        companion object DrawerScreen : Route.DrawerScreen()

        object Subjects : Route.DrawerScreen("subjects")
        object Timetable : Route.DrawerScreen("timetable")
        object About : Route.DrawerScreen("about")
        object Hostel : Route.DrawerScreen("hostel")
        object PaidServices : Route.DrawerScreen("paid_services")
        open class News private constructor(route: String = "news") : Route.DrawerScreen(route)
        {
            companion object News : Route.DrawerScreen.News()

            object NewsList : Route.DrawerScreen.News("news_list")
            open class NewsDetail(id: String) : Route.DrawerScreen.News("news_detail/$id")
            {

                companion object NewsDetail : Route.DrawerScreen.News.NewsDetail("{id}")

                override val navArguments: List<NamedNavArgument>
                    get() = listOf(navArgument("id") {
                        type = NavType.IntType
                    })

                override fun getArguments(entry: NavBackStackEntry): Int
                {
                    return entry.arguments!!.getInt("id")

                }
            }
        }
    }

    object SettingsScreen : Route("settings")

}