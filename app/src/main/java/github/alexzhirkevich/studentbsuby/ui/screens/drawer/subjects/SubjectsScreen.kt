package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBarSwipeRefreshIndicator
import github.alexzhirkevich.studentbsuby.ui.common.DefaultTextInput
import github.alexzhirkevich.studentbsuby.ui.common.ErrorScreen
import github.alexzhirkevich.studentbsuby.ui.common.ErrorWidget
import github.alexzhirkevich.studentbsuby.ui.common.FlowBox
import github.alexzhirkevich.studentbsuby.ui.common.NavigationMenuButton
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.Updatable
import github.alexzhirkevich.studentbsuby.util.applyIf
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import github.alexzhirkevich.studentbsuby.util.valueOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun SubjectsScreen(
    isTablet: Boolean,
    subjectsViewModel: SubjectsViewModel = hiltViewModel(),
    onMenuClicked: () -> Unit
                  )
{

    val data by subjectsViewModel.subjectsCommunication.collectAsState()

    when (val subjects = data)
    {
        is DataState.Success<*>, is DataState.Loading ->
        {
            SuccessSubjectsScreen(
                isTablet = isTablet,
                subjectsViewModel = subjectsViewModel,
                subjects = subjects,
                onMenuClicked = onMenuClicked,
                                 )
        }

        is DataState.Empty                            -> Box(
            modifier = Modifier.fillMaxSize()
                                                            ) {
            ErrorScreen(
                isTablet = isTablet,
                toolbarText = stringResource(id = R.string.subjects),
                title = stringResource(id = R.string.empty),
                error = stringResource(R.string.subjects_empty),
                updater = subjectsViewModel,
                onMenuClicked = onMenuClicked
                       )
        }

        is DataState.Error                            -> Box(
            modifier = Modifier.fillMaxSize()
                                                            ) {

            ErrorScreen(
                isTablet = isTablet,
                toolbarText = stringResource(id = R.string.subjects),
                title = stringResource(R.string.something_gone_wrong),
                error = stringResource(id = subjects.message),
                updater = subjectsViewModel,
                onMenuClicked = onMenuClicked
                       )
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
private fun SuccessSubjectsScreen(
    isTablet: Boolean,
    subjectsViewModel: SubjectsViewModel,
    subjects: DataState<List<List<Subject>>>,
    onMenuClicked: () -> Unit,
                                 )
{
    val scaffoldState = rememberCollapsingToolbarScaffoldState()


    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .zIndex(2f)
              )

        CollapsingToolbarScaffold(modifier = Modifier
            .zIndex(1f)
            .bsuBackgroundPattern(
                color = MaterialTheme.colors.primary.copy(alpha = .05f)
                                 ),
            state = scaffoldState,
            scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
            toolbarModifier = Modifier.background(MaterialTheme.colors.primary),
            toolbar = {
                Toolbar(
                    isTablet = isTablet,
                    viewModel = subjectsViewModel,
                    onMenuClicked = onMenuClicked
                       )
            }) {
            val searchText by subjectsViewModel.searchCommunication.collectAsState()
            val isUpdating by subjectsViewModel.isUpdating.collectAsState()

            val visibleSubjects by subjectsViewModel.visibleSubjectsCommunication.collectAsState()

            if (subjects is DataState.Success)
            {
                if (searchText.isNotBlank()) SearchSubjects(
                    visibleSubjects = visibleSubjects.valueOrNull().orEmpty(),
                    searchText = searchText
                                                           )
                else
                {
                    val currentSemester by subjectsViewModel.semesterCommunication.collectAsState()

                    AllSemesters(
                        initialSemester = currentSemester,
                        visibleSubjects = visibleSubjects.valueOrNull().orEmpty(),
                        subjects = subjects.valueOrNull().orEmpty(),
                        updater = subjectsViewModel,
                        isRefreshing = isUpdating
                                ) {
                        subjectsViewModel.handle(SubjectsEvent.SelectedSemesterChanged(it))
                    }
                }
            } else
            {
                Box(
                    modifier = Modifier.fillMaxSize()
                   ) {
                    BsuProgressBar(
                        Modifier.align(Alignment.Center),
                        size = 100.dp,
                        tint = MaterialTheme.colors.primary
                                  )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoroutinesApi

@Composable
private fun Toolbar(
    isTablet: Boolean, viewModel: SubjectsViewModel, onMenuClicked: () -> Unit
                   )
{

    var filtersVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val searchText by viewModel.searchCommunication.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(
            MaterialTheme.colors.secondary
                                                                                          )
          ) {

        TopAppBar(
            elevation = 0.dp, backgroundColor = Color.Transparent
                 ) {

            val focusRequester = remember {
                FocusRequester()
            }

            if (!isTablet)
            {
                NavigationMenuButton(onClick = onMenuClicked)
            }
            DefaultTextInput(value = searchText,
                onValueChange = {
                    viewModel.handle(SubjectsEvent.SubjectsSearchChanged(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                textInputModifier = Modifier.focusRequester(focusRequester),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                                                 ),
                leadingIcon = {
                    if (searchText.isEmpty())
                    {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                            )
                    } else
                    {
                        Icon(imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.handle(
                                        SubjectsEvent.CancelSearchCliched(
                                            focusRequester
                                                                         )
                                                    )
                                })
                    }
                },
                trailingIcon = {

                    Icon(painter = painterResource(
                        if (filtersVisible) R.drawable.ic_filter_list_off
                        else R.drawable.ic_filter_list
                                                  ),
                        contentDescription = "Search",
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { filtersVisible = !filtersVisible })
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.subjects))
                })
        }
        AnimatedVisibility(visible = filtersVisible) {
            Row(
                modifier = Modifier.animateEnterExit(
                    enter = slideInVertically(), exit = slideOutVertically()
                                                    )
               ) {

                val withCredit by viewModel.withCreditCommunication.collectAsState()
                val withExam by viewModel.withExamCommunication.collectAsState()

                Row(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .clip(CircleShape)
                        .clickable {
                            viewModel.handle(SubjectsEvent.SubjectsWithCreditFilterPressed)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                   ) {
                    Checkbox(checked = withCredit,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            checkmarkColor = MaterialTheme.colors.onPrimary
                                                        ),
                        onCheckedChange = {
                            viewModel.handle(SubjectsEvent.SubjectsWithCreditFilterPressed)
                        })
                    Text(text = stringResource(id = R.string.with_credit))
                }
                Row(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .clip(CircleShape)
                        .clickable {
                            viewModel.handle(SubjectsEvent.SubjectsWithExamFilterPressed)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                   ) {
                    Checkbox(checked = withExam,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary,
                            checkmarkColor = MaterialTheme.colors.onPrimary
                                                        ),
                        onCheckedChange = {
                            viewModel.handle(SubjectsEvent.SubjectsWithExamFilterPressed)
                        })
                    Text(text = stringResource(id = R.string.with_exam))
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun SearchSubjects(
    visibleSubjects: List<List<Subject>>, searchText: String
                          )
{
    Column(Modifier.fillMaxSize()) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray.copy(alpha = .3f))
               )
        if (visibleSubjects.flatten().isEmpty())
        {
            Box(Modifier.fillMaxSize()) {
                ErrorWidget(
                    title = stringResource(id = R.string.empty),
                    error = stringResource(id = R.string.subjects_not_found_search, searchText),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp)
                           )
            }
        } else
        {
            val openedSubject = remember { mutableStateListOf<Subject>() }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .navigationBarsPadding()
                    .imePadding()
                      ) {
                stickyHeader {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                       ) {
                        Text(
                            text = stringResource(id = R.string.semester, openedSubject.size + 1),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colors.background.copy(.9f))
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                                .align(Alignment.TopCenter)
                            )
                    }
                }

                itemsIndexed(visibleSubjects) { _, list ->
                    if (list.isNotEmpty())
                    {
                        Page(
                            subjects = list,
                            opened = openedSubject,
                            modifier = Modifier.fillMaxWidth()
                            )
                    }
                }

                item {
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun AllSemesters(
    initialSemester: Int,
    visibleSubjects: List<List<Subject>>,
    subjects: List<List<Subject>>,
    updater: Updatable,
    isRefreshing: Boolean,
    onSemesterChanged: (Int) -> Unit
                        )
{
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (state.pageCount > initialSemester) state.scrollToPage(initialSemester)
    }

    LaunchedEffect(key1 = state.currentPage) {
        onSemesterChanged(state.currentPage)
    }

    Scaffold(backgroundColor = Color.Transparent, topBar = {
        AllTermsTopBar(state, subjects, scope)
    }) {
        SubjectsPager(isRefreshing, updater, subjects, state, visibleSubjects)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun SubjectsPager(
    isRefreshing: Boolean,
    updater: Updatable,
    subjects: List<List<Subject>>,
    state: PagerState,
    visibleSubjects: List<List<Subject>>
                         )
{
    val refreshState = rememberSwipeRefreshState(
        isRefreshing = isRefreshing
                                                )

    val openedSubjects = remember {
        mutableStateListOf<Subject>()
    }
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        indicator = { state, trigger ->
            BsuProgressBarSwipeRefreshIndicator(state = state, trigger = trigger)
        },
        state = refreshState,
        onRefresh = updater::update,
                ) {
        HorizontalPager(
            count = subjects.size,
            state = state,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
                       ) { page ->
            if (visibleSubjects.getOrNull(page)?.isNotEmpty() == true)
            {
                Page(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .imePadding()
                        .graphicsLayer {
                            translationY = refreshState.indicatorOffset
                        },
                    subjects = visibleSubjects[page],
                    opened = openedSubjects,
                    withBottomPadding = true,
                    )
            } else
            {
                Box(Modifier.fillMaxSize()) {
                    ErrorWidget(
                        title = stringResource(id = R.string.empty),
                        error = stringResource(id = R.string.subjects_not_found),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 100.dp)
                               )
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun AllTermsTopBar(
    state: PagerState, subjects: List<List<Subject>>, scope: CoroutineScope
                          )
{
    ScrollableTabRow(selectedTabIndex = state.currentPage,
        backgroundColor = MaterialTheme.colors.secondary,
        edgePadding = 0.dp,
        modifier = Modifier.fillMaxWidth(),
        indicator = { tabs ->
            Box {
                Spacer(modifier = Modifier
                    .applyIf(state.pageCount == tabs.size) {
                        it.pagerTabIndicatorOffset(
                            pagerState = state, tabs
                                                  )
                    }
                    .height(2.dp)
                    .zIndex(Float.MAX_VALUE)
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colors.primary))
            }
        }) {
        for (i in subjects.indices)
        {
            Tab(selected = state.currentPage == i, onClick = {
                scope.launch {
                    state.scrollToPage(i)
                }
            }) {
                Text(
                    text = stringResource(id = R.string.semester_short, i + 1),
                    modifier = Modifier.padding(vertical = 10.dp)
                    )
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
private fun Page(
    subjects: List<Subject>,
    opened: SnapshotStateList<Subject>,
    modifier: Modifier = Modifier,
    withBottomPadding: Boolean = false,
                )
{
    FlowBox(
        elementWidth = 180.dp, modifier = modifier
           ) {
        subjects.forEach {
            val isOpened by derivedStateOf {
                it in opened
            }
            SubjectWidget(
                subject = it, isOpened = isOpened, modifier = Modifier.padding(5.dp)
                         ) {
                if (isOpened)
                {
                    opened.remove(it)
                } else opened.add(it)
            }
        }
        if (withBottomPadding)
        {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}