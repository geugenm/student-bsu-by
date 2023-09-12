package github.alexzhirkevich.studentbsuby.ui.screens.drawer.news

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.statusBarsHeight
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.navigation.Route
import github.alexzhirkevich.studentbsuby.ui.common.NavigationMenuButton
import github.alexzhirkevich.studentbsuby.ui.common.animatedComposable
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun NewsScreen(
    isTablet: Boolean,
    viewModel: NewsViewModel = hiltViewModel(),
    onMenuClicked: () -> Unit,
              )
{

    val navController = rememberNavController()

    val items = remember {
        listOf(
            Route.DrawerScreen.News.NewsList, Route.DrawerScreen.News.NewsDetail
              )
    }

    var currentRoute by rememberSaveable {
        mutableStateOf(Route.DrawerScreen.News.NewsList.route)
    }

    val scaffoldState = rememberCollapsingToolbarScaffoldState()


    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsHeight()
                .background(MaterialTheme.colors.secondary)
                .zIndex(1f)
              )
        CollapsingToolbarScaffold(modifier = Modifier
            .fillMaxSize()
            .bsuBackgroundPattern(
                color = MaterialTheme.colors.primary.copy(alpha = .05f)
                                 ),
            state = scaffoldState,
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbar = {
                Toolbar(
                    isTablet = isTablet,
                    inDetail = currentRoute != Route.DrawerScreen.News.NewsList.route,
                    onMenuClicked
                       )
            }) {
            NavHost(navController = navController, startDestination = items[0].route) {

                animatedComposable(Route.DrawerScreen.News.NewsList) {
                    currentRoute = Route.DrawerScreen.News.NewsList.route
                    NewsListScreen(
                        viewModel = viewModel,
                        navController = navController,
                                  )

                }

                animatedComposable(Route.DrawerScreen.News.NewsDetail) {
                    currentRoute = Route.DrawerScreen.News.NewsDetail.route
                    val id = Route.DrawerScreen.News.NewsDetail.getArguments(it)

                    NewsDetailsScreen(
                        id = id, viewModel = viewModel
                                     )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun Toolbar(isTablet: Boolean, inDetail: Boolean, onMenuClicked: () -> Unit)
{
    val activity = LocalContext.current as Activity

    Column {
        TopAppBar(
            modifier = Modifier.zIndex(1f),
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.secondary
                 ) {
            AnimatedContent(targetState = inDetail, transitionSpec = {
                (scaleIn() + fadeIn() with scaleOut() + fadeOut())
            }) { inDetail ->
                if (inDetail)
                {
                    NavigationMenuButton(
                        icon = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        onClick = activity::onBackPressed
                                        )
                } else
                {
                    if (!isTablet)
                    {
                        NavigationMenuButton(onClick = onMenuClicked)
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.news),
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle1
                )
        }

        Spacer(
            modifier = Modifier
                .height(1f.dp)
                .fillMaxWidth()
                .background(LocalContentColor.current.copy(.1f))
              )
    }
}