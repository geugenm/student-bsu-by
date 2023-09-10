package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBarSwipeRefreshIndicator
import github.alexzhirkevich.studentbsuby.ui.common.ErrorWidget
import github.alexzhirkevich.studentbsuby.ui.common.NavigationMenuButton
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.animatedSquaresBackground
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.CollapsingToolbarState
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

private const val TabsHeight = 40

@ExperimentalToolbarApi
@ExperimentalPagerApi
@ExperimentalMaterialApi

@Composable
fun TimetableScreen(
    isTablet: Boolean,
    timetableViewModel: TimetableViewModel = hiltViewModel(),
    onMenuClicked: () -> Unit = {},
) {

    val scaffoldState = rememberCollapsingToolbarScaffoldState(
        toolbarState = rememberCollapsingToolbarState(0)
    )

    val pagerState = rememberPagerState()

    LaunchedEffect(Unit) {
        scaffoldState.toolbarState.collapse(0)
        scaffoldState.toolbarState.expand(500)
    }

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .bsuBackgroundPattern(
                MaterialTheme.colors.primary.copy(alpha = .05f),
                true
            ),
        state = scaffoldState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Toolbar(
                isTablet = isTablet,
                toolbarState = scaffoldState.toolbarState,
                viewModel = timetableViewModel,
                onMenuClicked = onMenuClicked,
                pagerState = pagerState,
            )
        }
    ) {
        val swipeDownEnabled by remember {
            derivedStateOf {
                scaffoldState.toolbarState.progress >= 1f - Float.MIN_VALUE
            }
        }
        Body(
            viewModel = timetableViewModel,
            pagerState = pagerState,
            swipeDownEnabled = swipeDownEnabled
        )
    }
}


@ExperimentalPagerApi
@Composable
private fun CollapsingToolbarScope.Toolbar(
    isTablet: Boolean,
    toolbarState: CollapsingToolbarState,
    pagerState: PagerState,
    viewModel: TimetableViewModel,
    onMenuClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()

    val timetable by viewModel.timetableCommunication.collectAsState()

    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .zIndex(1f)
            .statusBarsPadding(),
        backgroundColor = Color.Transparent
    ) {
        val status = LocalWindowInsets.current.statusBars.bottom
        val density = LocalDensity.current

        val iconVisible by remember {
            derivedStateOf {
                toolbarState.height > 3 * TabsHeight * density.density + status
            }
        }
        AnimatedVisibility(
            visible = iconVisible,
            enter = slideInVertically { -2 * it },
            exit = slideOutVertically { -2 * it }
        ) {
            if (!isTablet) {
                NavigationMenuButton(onClick = onMenuClicked)
            }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .parallax(.5f)
            .background(MaterialTheme.colors.secondary)
            .graphicsLayer {
                alpha = toolbarState.progress
            }
            .animatedSquaresBackground(
                color = MaterialTheme.colors.primary.copy(alpha = .05f),
                count = 10,
                size = 200.dp
            )
    ) {
        Row(
            Modifier
                .align(Alignment.Center)
                .padding(top = 30.dp + TabsHeight.dp / 2, bottom = 30.dp + TabsHeight.dp / 2),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = viewModel.dayOfMonth.toString(),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colors.primary
            )
            Column {
                Text(
                    text = stringArrayResource(id = R.array.weekdays)
                        [viewModel.dayOfWeek],
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary

                )
                Text(
                    text = "${stringArrayResource(id = R.array.months)[viewModel.month]} ${viewModel.year}".uppercase(),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary

                )
            }
        }
    }

    Box(
        Modifier
            .zIndex(2f)
            .road(
                whenExpanded = Alignment.BottomCenter,
                whenCollapsed = Alignment.BottomCenter
            )

    ) {

        Spacer(modifier = Modifier.statusBarsHeight(TabsHeight.dp))
        TabRow(
            modifier = Modifier
                .height(TabsHeight.dp)
                .align(Alignment.BottomCenter),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Transparent,
            indicator = { tabs ->
                Box {
                    Spacer(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabs)
                            .height(2.dp)
                            .background(MaterialTheme.colors.primary)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        ) {
            val weekdays = stringArrayResource(id = R.array.weekdays_short)
            for (i in 0..5) {
                Tab(
                    selected = pagerState.currentPage == i,
                    onClick = {
                        if (timetable is DataState.Success<*>) {
                            scope.launch {
                                kotlin.runCatching {
                                    pagerState.scrollToPage(i)
                                }
                            }
                        }
                    }) {
                    Text(
                        text = weekdays[i],
                    )
                }
            }
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalPagerApi

@Composable
private fun Body(
    viewModel: TimetableViewModel,
    pagerState: PagerState,
    swipeDownEnabled: Boolean,
) {

    val timetable by viewModel.timetableCommunication.collectAsState()

    val refreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isUpdating.collectAsState().value
    )


    SwipeRefresh(
        state = refreshState,
        onRefresh = viewModel::update,
        indicator = { state, trigger ->
            BsuProgressBarSwipeRefreshIndicator(state, trigger)
        },
        swipeEnabled = swipeDownEnabled && timetable !is DataState.Loading,
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val tt = timetable) {
            is DataState.Success -> {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    count = tt.value.size
                ) {

                    if (tt.value[it].isNotEmpty()) {
                        TimetableWidget(
                            list = tt.value[it],
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    translationY = refreshState.indicatorOffset
                                }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            ErrorWidget(
                                modifier = Modifier
                                    .padding(top = 70.dp)
                                    .align(Alignment.TopCenter),
                                title = stringResource(id = R.string.empty),
                                error = stringResource(id = R.string.timetable_empty_for_day)
                            )
                        }
                    }
                }
            }

            DataState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ErrorWidget(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 70.dp),
                        title = stringResource(id = R.string.empty),
                        error = stringResource(id = R.string.timetable_empty)
                    )
                }
            }

            is DataState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ErrorWidget(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 70.dp),
                        title = stringResource(id = R.string.something_gone_wrong),
                        error = stringResource(id = tt.message)
                    )
                }
            }

            DataState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    BsuProgressBar(
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 100.dp),
                        size = 100.dp,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}
