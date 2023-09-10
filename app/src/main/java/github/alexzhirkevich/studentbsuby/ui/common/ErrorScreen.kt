package github.alexzhirkevich.studentbsuby.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.util.Updatable
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState

@Composable
fun ErrorScreen(
    isTablet: Boolean,
    toolbarText: String,
    error: String,
    updater: Updatable,
    title: String = stringResource(id = R.string.something_gone_wrong),
    onMenuClicked: () -> Unit = {}
               )
{
    Column(
        Modifier
            .background(MaterialTheme.colors.secondary)
            .zIndex(2f)
          ) {
        Spacer(modifier = Modifier.statusBarsHeight())
        TopAppBar(
            elevation = 0.dp, backgroundColor = Color.Transparent
                 ) {
            if (!isTablet)
            {
                NavigationMenuButton(onClick = onMenuClicked)
            }
            Text(
                text = toolbarText,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle1
                )
        }
    }

    val status = LocalWindowInsets.current.statusBars.bottom
    with(LocalDensity.current) {

        val isUpdating by updater.isUpdating.collectAsState()

        SwipeRefresh(
            state = rememberSwipeRefreshState(
                isRefreshing = isUpdating
                                             ),
            indicator = { state, offset ->
                BsuProgressBarSwipeRefreshIndicator(state = state, trigger = offset)
            },
            indicatorPadding = PaddingValues(top = status.toDp() + 75.dp),
            onRefresh = updater::update
                    ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colors.background)
               ) {
                ErrorWidget(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 30.dp),
                    title = title,
                    error = error
                           )
            }
        }
    }
}

@Composable
fun ErrorWidget(title: String, error: String, modifier: Modifier = Modifier)
{

    Column(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
          ) {
        Image(
            painter = painterResource(id = R.drawable.error), contentDescription = ""
             )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title, style = MaterialTheme.typography.h2, textAlign = TextAlign.Center
            )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = error, style = MaterialTheme.typography.body1, textAlign = TextAlign.Center

            )
    }
}