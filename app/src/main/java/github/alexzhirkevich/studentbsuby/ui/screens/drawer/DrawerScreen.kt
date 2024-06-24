@file:OptIn(ExperimentalMaterialApi::class)

package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animate
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.User
import github.alexzhirkevich.studentbsuby.ui.common.animatedComposable
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.about.AboutScreen
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.hostel.HostelScreen
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.news.NewsScreen
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices.PaidServicesScreen
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects.SubjectsScreen
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable.TimetableScreen
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import github.alexzhirkevich.studentbsuby.util.valueOrNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import me.onebone.toolbar.ExperimentalToolbarApi
import kotlin.math.absoluteValue

private val AvatarWidth = 150.dp
private val AvatarHeight = 200.dp
private val AvatarOffset = AvatarHeight / 3.5f
private val CardPaddings = 12.dp


@ExperimentalComposeUiApi
@ExperimentalToolbarApi
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@Composable
fun DrawerScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
                )
{

    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                                             )
    val scope = rememberCoroutineScope()

    val childNavController = rememberNavController()
    val initial = DrawerRoute.Timetable

    val activity = LocalContext.current as ComponentActivity

    val isTablet = rememberSaveable {
        activity.resources.getBoolean(R.bool.is_tablet)
    }

    val routes = remember {
        listOf(
            DrawerRoute.News,
            DrawerRoute.Subjects,
            DrawerRoute.Timetable,
            DrawerRoute.Hostel,
            DrawerRoute.PaidServices,
            DrawerRoute.About,
              )
    }

    val connection by profileViewModel.connectivityCommunication.collectAsState(initial = ConnectivityUi.Connected)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.background(MaterialTheme.colors.secondary),
        drawerShape = DrawerShape,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        snackbarHost = {
            ConnectivitySnackBar(
                connection = connection
                                ) {
                profileViewModel.handle(ProfileEvent.Logout(navController))
            }
        },
        drawerContent = {
            if (!isTablet)
            {
                DrawerContent(
                    navController = navController,
                    childNavController = childNavController,
                    routes = routes,
                    profileViewModel = profileViewModel
                             ) {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            }
        },
            ) { it ->


        fun onMenuClicked()
        {
            scope.launch {
                scaffoldState.drawerState.let {
                    if (it.isClosed) it.open()
                    else it.close()
                }
            }
        }

        Row(modifier = Modifier.padding(it)) {
            if (isTablet)
            {
                Card(
                    elevation = 5.dp, shape = RectangleShape
                    ) {
                    DrawerContent(
                        navController = navController,
                        childNavController = childNavController,
                        routes = routes,
                        profileViewModel = profileViewModel
                                 )
                }
            }
            NavHost(
                navController = childNavController, startDestination = initial.route.route
                   ) {

                val animIn = scaleIn(initialScale = .95f) + fadeIn()
                val animOut = scaleOut(targetScale = .95f) + fadeOut()


                animatedComposable(DrawerRoute.News.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    NewsScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }
                animatedComposable(DrawerRoute.Subjects.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    SubjectsScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }

                animatedComposable(DrawerRoute.Timetable.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    TimetableScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }

                animatedComposable(DrawerRoute.Hostel.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    HostelScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }

                animatedComposable(DrawerRoute.About.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    AboutScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }

                animatedComposable(DrawerRoute.PaidServices.route,
                    enterTransition = { animIn },
                    exitTransition = { animOut }) {
                    PaidServicesScreen(isTablet, onMenuClicked = ::onMenuClicked)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ConnectivitySnackBar(
    connection: ConnectivityUi, onRelogin: () -> Unit
                        )
{
    if (connection != ConnectivityUi.Connected)
    {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .fillMaxWidth()
           ) {
            Card(
                elevation = 5.dp,
                backgroundColor = MaterialTheme.colors.error,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(10.dp)
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(3.dp)
                   ) {
                    var expanded by rememberSaveable {
                        mutableStateOf(true)
                    }
                    if (expanded)
                    {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    vertical = 5.dp, horizontal = 10.dp
                                        )
                              ) {
                            Text(
                                text = stringResource(id = R.string.offline),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.onError
                                )
                            Text(
                                text = stringResource(
                                    id = if (connection == ConnectivityUi.Offline) R.string.relogin else R.string.connecting
                                                     ),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onError
                                )
                        }
                        if (connection == ConnectivityUi.Offline)
                        {
                            IconButton(onClick = onRelogin) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Login,
                                    contentDescription = null
                                    )
                            }
                        }
                    }
                    IconButton(onClick = {
                        expanded = !expanded
                    }) {
                        AnimatedContent(targetState = expanded, label = "") {
                            Icon(
                                imageVector = if (it) Icons.Default.Close
                                else Icons.Default.CloudOff, contentDescription = null
                                )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalToolbarApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
private fun DrawerContent(
    navController: NavController,
    childNavController: NavController,
    routes: List<DrawerRoute>,
    profileViewModel: ProfileViewModel,
    onRouteSelected: () -> Unit = {}
                         )
{

    Column(
        Modifier
            .background(MaterialTheme.colors.secondary)
            .widthIn(max = DrawerShape.width.dp)
          ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding(),
              ) {

            Box(
                Modifier
                    .clip(ProfileRoundShape())
                    .background(MaterialTheme.colors.primaryVariant)
                    .bsuBackgroundPattern(
                        MaterialTheme.colors.onPrimary.copy(alpha = .1f)
                                         )
                    .statusBarsPadding()
               ) {

                SelectionContainer {
                    ProfileCard(
                        photo = profileViewModel.imageCommunication.collectAsState().value,
                        user = profileViewModel.userCommunication.collectAsState().value,
                        modifier = Modifier.padding(CardPaddings)
                               )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Column {
                routes.forEach {
                    DrawerButton(icon = it.icon, text = stringResource(id = it.title), onClick = {
                        onRouteSelected()
                        profileViewModel.handle(ProfileEvent.RouteSelected(it, childNavController))
                    })
                }
                Spacer(
                    modifier = Modifier
                        .height(.5.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.typography.caption.color)
                      )
                DrawerButton(icon = Icons.Default.Settings,
                    text = stringResource(id = R.string.settings),
                    onClick = {
                        onRouteSelected()
                        profileViewModel.handle(ProfileEvent.SettingsClicked(navController))
                    })

            }
            Spacer(modifier = Modifier.weight(1f))

            DrawerButton(icon = Icons.AutoMirrored.Filled.Logout,
                text = stringResource(id = R.string.logout),
                onClick = {
                    profileViewModel.handle(ProfileEvent.Logout(navController))
                })
        }
    }
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
                        )
{

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            color = MaterialTheme.colors.primary,
                                                           )
                            ) {
        Row(
            modifier
                .clickable(onClick = onClick)
                .heightIn(min = 45.dp)
                .padding(
                    horizontal = 15.dp, vertical = 5.dp
                        ),
            verticalAlignment = Alignment.CenterVertically
           ) {
            Icon(
                imageVector = icon,
                contentDescription = "Menu Icon",
                tint = MaterialTheme.colors.primary,
                )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.weight(1f)
                )
            content()
        }
    }
}

@Composable
private fun ProfileCard(
    photo: DataState<ImageBitmap>, user: DataState<User>, modifier: Modifier = Modifier
                       )
{


    Box(modifier) {
        Row(modifier = Modifier.zIndex(2f)) {
            Card(
                shape = MaterialTheme.shapes.medium,
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = 5.dp,
                modifier = Modifier
                ) {
                var dialogVisible by rememberSaveable {
                    mutableStateOf(false)
                }
                val photoValue = photo.valueOrNull()
                if (photoValue != null)
                {
                    if (dialogVisible)
                    {
                        Dialog(onDismissRequest = {
                            dialogVisible = false
                        }) {
                            var offsetY by remember {
                                mutableStateOf(0f)
                            }
                            val dragState = remember {
                                DraggableState {
                                    offsetY += it
                                }
                            }
                            val density = LocalDensity.current

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)

                                    .draggable(state = dragState,
                                        orientation = Orientation.Vertical,
                                        onDragStopped = {
                                            if (offsetY.absoluteValue > 75 * density.density)
                                            {
                                                dialogVisible = false
                                            } else
                                            {
                                                animate(offsetY, 0f) { a, _ ->
                                                    offsetY = a
                                                }
                                            }
                                        })
                               ) {

                                Image(bitmap = photoValue,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Photo",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .aspectRatio(AvatarWidth / AvatarHeight)
                                        .fillMaxSize()
                                        .offset {
                                            IntOffset(0, offsetY.toInt())
                                        }

                                     )
                            }
                        }
                    }

                    Image(bitmap = photoValue,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Photo",
                        modifier = Modifier
                            .width(AvatarWidth)
                            .height(AvatarHeight)
                            .clickable {
                                dialogVisible = true
                            })
                } else Spacer(
                    modifier = Modifier
                        .width(AvatarWidth)
                        .height(AvatarHeight)
                             )
            }
            Box(
                Modifier
                    .height(AvatarOffset)
                    .weight(1f)
                    .padding(10.dp)
               ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_text),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                     )
            }
        }

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = 5.dp,
            backgroundColor = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AvatarOffset)
                .zIndex(1f)
            ) {

            val userValue = user.valueOrNull() ?: User()

            Column(
                modifier = Modifier.padding(CardPaddings)
                  ) {

                Column(
                    Modifier.heightIn(min = AvatarHeight - AvatarOffset + 5.dp)
                      ) {

                    Text(
                        text = userValue.name.toString(),
                        modifier = Modifier.padding(start = AvatarWidth),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.subtitle1
                        )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = userValue.faculty.toString(),
                        modifier = Modifier.padding(start = AvatarWidth),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.caption
                        )
                }
                Text(
                    text = userValue.info.toString(),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body1,
                    )
                Text(
                    text = userValue.avgGrade.toString(),
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold
                    )
            }
        }
    }
}