package github.alexzhirkevich.studentbsuby.ui.screens.drawer.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.navigation.Route
import github.alexzhirkevich.studentbsuby.navigation.navigate
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBarSwipeRefreshIndicator
import github.alexzhirkevich.studentbsuby.ui.common.ErrorWidget
import github.alexzhirkevich.studentbsuby.ui.common.NavigationMenuButton
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState

@ExperimentalMaterialApi
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel, navController: NavController
                  )
{
    val refreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isUpdating.collectAsState().value
                                                )


    val news by viewModel.newsCommunication.collectAsState()

    SwipeRefresh(state = refreshState,
        onRefresh = viewModel::update,
        indicator = { state, trigger ->
            BsuProgressBarSwipeRefreshIndicator(state = state, trigger = trigger)
        }) {
        when (val n = news)
        {
            is DataState.Success -> Body(news = n.value, modifier = Modifier.graphicsLayer {
                translationY = refreshState.indicatorOffset
            }) {
                navController.navigate(Route.DrawerScreen.News.NewsDetail(it.id.toString()))
            }

            is DataState.Empty   -> Error(
                title = stringResource(id = R.string.empty),
                error = stringResource(id = R.string.news_empty)
                                         )

            is DataState.Error   -> Error(
                title = stringResource(id = R.string.something_gone_wrong),
                error = stringResource(id = n.message)
                                         )

            is DataState.Loading -> Loading()
        }
    }
}

@Composable
private fun Toolbar(isTablet: Boolean, onMenuClicked: () -> Unit)
{
    Column {
        TopAppBar(
            modifier = Modifier.zIndex(1f),
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.secondary
                 ) {
            if (!isTablet)
            {
                NavigationMenuButton(onClick = onMenuClicked)
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

@ExperimentalMaterialApi
@Composable
private fun Body(
    news: List<News>, modifier: Modifier = Modifier, onClick: (News) -> Unit = {}
                )
{
    LazyColumn(
        modifier.fillMaxSize(),
              ) {
        items(news.size) {
            NewsWidget(
                news = news[it], modifier = Modifier.padding(5.dp)
                      ) {
                onClick(news[it])
            }
        }
        item { Spacer(modifier = Modifier.navigationBarsPadding().imePadding()) }
    }
}

@ExperimentalMaterialApi
@Composable
private fun NewsWidget(news: News, modifier: Modifier = Modifier, onClick: () -> Unit)
{
    Card(
        backgroundColor = MaterialTheme.colors.secondary, modifier = modifier, onClick = onClick
        ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
              ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold
                )
            news.preview?.let {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSecondary
                    )
            }
        }
    }
}

@Composable
private fun Loading()
{
    Box(
        modifier = Modifier.fillMaxSize()
       ) {
        BsuProgressBar(
            Modifier.align(Alignment.Center), size = 100.dp, tint = MaterialTheme.colors.primary
                      )
    }
}

@Composable
private fun Error(
    title: String, error: String
                 )
{

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

       ) {
        ErrorWidget(
            title = title,
            error = error,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                   )
    }
}