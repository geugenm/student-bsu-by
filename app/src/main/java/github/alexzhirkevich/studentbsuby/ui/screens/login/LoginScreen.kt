package github.alexzhirkevich.studentbsuby.ui.screens.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.pager.ExperimentalPagerApi
import de.charlex.compose.HtmlText
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar
import github.alexzhirkevich.studentbsuby.ui.common.DefaultTextInput
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.about.AboutEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.about.AboutViewModel
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import github.alexzhirkevich.studentbsuby.util.valueOrNull
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val ButtonWidth = 200

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    aboutViewModel: AboutViewModel = hiltViewModel()
) {

    val controlsEnabled by loginViewModel.controlsEnabled.collectAsState()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        loginViewModel.error.collect {
            if (it.isNotBlank())
                scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        snackbarHost = { snack ->
            SnackbarHost(
                snack,
                modifier = Modifier
                    .navigationBarsWithImePadding()

            ) {
                Card(
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = it.message,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onError
                    )
                }
            }
        }
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .bsuBackgroundPattern(
                    MaterialTheme.colors.primary
                        .copy(alpha = .05f)
                )
        ) {

            Column(
                Modifier
                    .widthIn(max = 400.dp)
                    .align(Alignment.Center)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 20.dp)
                    .navigationBarsWithImePadding()

            ) {
                Spacer(
                    modifier = Modifier
                        .height(45.dp)
                )

                Box {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-40).dp)
                            .size(80.dp)
                            .zIndex(2f),
                        elevation = 3.dp,
                        backgroundColor = MaterialTheme.colors.secondary,
                        shape = CircleShape
                    ) {
                        Box(
                            Modifier
                                .padding(5.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Login",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Card(
                        elevation = 3.dp,
                        backgroundColor = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(horizontal = 35.dp)
                            .zIndex(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                elevation = 2.dp,
                                backgroundColor = MaterialTheme.colors.secondary,
                                shape = object : Shape {

                                    override fun createOutline(
                                        size: Size,
                                        layoutDirection: LayoutDirection,
                                        density: Density
                                    ): Outline {
                                        val cr = CornerRadius(
                                            (size.width - ButtonWidth * density.density * 0.75f) / 2,
                                            size.height / 2
                                        )
                                        return Outline.Rounded(
                                            RoundRect(
                                                top = 0f, left = 0f,
                                                bottom = size.height, right = size.width,
                                                bottomLeftCornerRadius = cr,
                                                bottomRightCornerRadius = cr
                                            )
                                        )
                                    }
                                }
                            ) {

                                Box(
                                    Modifier
                                        .padding(
                                            top = 80.dp,
                                            bottom = 20.dp,
                                            start = 25.dp,
                                            end = 25.dp
                                        )
                                ) {
                                    LoginForm(
                                        loginViewModel = loginViewModel
                                    )
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            ) {
                                CompositionLocalProvider(
                                    LocalTextStyle provides MaterialTheme.typography.caption
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = loginViewModel.autoLogin.collectAsState().value,
                                            enabled = controlsEnabled,
                                            onCheckedChange = {
                                                loginViewModel.handle(LoginEvent.AutoLoginChanged(it))
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colors.primary,
                                                checkmarkColor = MaterialTheme.colors.onPrimary
                                            )
                                        )

                                        Text(text = stringResource(id = R.string.autologin))
                                    }

                                }

                                val keyboard = LocalSoftwareKeyboardController.current
                                Button(
                                    onClick = {
                                        loginViewModel.handle(LoginEvent.LoginClicked(navController))
                                        keyboard?.hide()
                                    },
                                    enabled = controlsEnabled,
                                    modifier = Modifier
                                        .width(ButtonWidth.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.btn_login),
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(3.dp))


                                var showDialog by rememberSaveable {
                                    mutableStateOf(false)
                                }
                                if (showDialog) {
                                    Dialog(onDismissRequest = { showDialog = false }) {
                                        Card(
                                            backgroundColor = MaterialTheme.colors.secondary
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(
                                                    horizontal = 10.dp,
                                                    vertical = 5.dp
                                                )
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.cant_login),
                                                    style = MaterialTheme.typography.subtitle1,
                                                    textAlign = TextAlign.Center,
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                HtmlText(
                                                    textId = R.string.cant_login_dialog,
                                                    style = MaterialTheme.typography.body1,
                                                    textAlign = TextAlign.Center,
                                                    urlSpanStyle = SpanStyle(
                                                        color = MaterialTheme.colors.primary,
                                                        textDecoration = TextDecoration.Underline
                                                    )
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))

                                                Row(
                                                    Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceAround,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    IconButton(
                                                        onClick = {
                                                            aboutViewModel.handle(AboutEvent.EmailClicked)
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Email,
                                                            contentDescription = "E-mail",
                                                            tint = MaterialTheme.colors.primary,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }

                                                    IconButton(
                                                        onClick = {
                                                            aboutViewModel.handle(AboutEvent.TgClicked)
                                                        }
                                                    ) {
                                                        Image(
                                                            painter = painterResource(R.drawable.ic_telegram),
                                                            contentDescription = "Telegram",
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }

                                                    IconButton(onClick = { showDialog = false }) {
                                                        Icon(
                                                            imageVector = Icons.Default.Close,
                                                            tint = MaterialTheme.colors.onSecondary,
                                                            contentDescription = "Close",
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                ClickableText(
                                    style = MaterialTheme.typography.caption,
                                    text = AnnotatedString(stringResource(R.string.cant_login))
                                ) {
                                    showDialog = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
private fun LoginForm(
    loginViewModel: LoginViewModel
) {

    val controlsEnabled by loginViewModel.controlsEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextInput(
            value = loginViewModel.login.collectAsState().value,
            onValueChange = {
                loginViewModel.handle(LoginEvent.LoginChanged(it))
            },
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = "Login")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            enabled = controlsEnabled,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.login),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(.5.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium)

        )

        Spacer(modifier = Modifier.height(10.dp))


        var passwordVisible by rememberSaveable {
            mutableStateOf(false)
        }
        DefaultTextInput(
            value = loginViewModel.password.collectAsState().value,
            onValueChange = {
                loginViewModel.handle(LoginEvent.PasswordChanged(it))
            },
            singleLine = true,

            maxLines = 1,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.VpnKey, contentDescription = "Password")
            },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Default.VisibilityOff
                    else Icons.Default.Visibility,
                    contentDescription = "Show password",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            },
            enabled = controlsEnabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),

            placeholder = {
                Text(
                    text = stringResource(id = R.string.password),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(.5.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(10.dp))

        val captcha by loginViewModel.captchaImage.collectAsState()

        Box(
            Modifier
                .background(
                    color = MaterialTheme.colors.background,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            if (captcha is DataState.Loading) {
                BsuProgressBar(
                    Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colors.primary,
                    size = 40.dp
                )
            }
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .border(.5.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val capchaModifier = remember {
                    Modifier
                        .height(44.dp)
                        .width((ButtonWidth - 44).dp)
//
                }

                when (captcha) {
                    is DataState.Success<*> -> Image(
                        bitmap = captcha.valueOrNull()!!,
                        contentDescription = "captcha",
                        contentScale = ContentScale.Crop,
                        modifier = capchaModifier,
                        colorFilter = if (!MaterialTheme.colors.isLight)
                            ColorFilter.colorMatrix(
                                colorMatrix = ColorMatrix(
                                    floatArrayOf(
                                        -1f, 0f, 0f, 0f, 255f,
                                        0f, -1f, 0f, 0f, 255f,
                                        0f, 0f, -1f, 0f, 255f,
                                        0f, 0f, 0f, 1f, 0f,
                                    )
                                )
                            ) else null
                    )

                    else -> Spacer(modifier = capchaModifier)
                }
                Box(
                    Modifier
                        .clickable {
                            loginViewModel.handle(LoginEvent.UpdateClicked())
                        }
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Update captcha",
                        modifier = Modifier
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        val keyboardController = LocalSoftwareKeyboardController.current
        DefaultTextInput(
            value = loginViewModel.captcha.collectAsState().value,
            onValueChange = {
                loginViewModel.handle(LoginEvent.CaptchaChanged(it))
            },
            singleLine = true,
            enabled = controlsEnabled,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.captcha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
            },
            maxLines = 1,
            modifier = Modifier
                .width(ButtonWidth.dp)
                .heightIn(min = 44.dp)
                .border(.5.dp, MaterialTheme.colors.primary, MaterialTheme.shapes.medium)

        )
    }
}