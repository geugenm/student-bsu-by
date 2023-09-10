package github.alexzhirkevich.studentbsuby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import github.alexzhirkevich.studentbsuby.repo.ApplicationVersion
import github.alexzhirkevich.studentbsuby.ui.screens.MainScreen
import github.alexzhirkevich.studentbsuby.ui.theme.StudentbsubyTheme
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.ExperimentalToolbarApi


@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity()
{

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mainActivityViewModel.handle(MainActivityEvent.Initialized(this))
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            StudentbsubyTheme {
                MainScreen()
                if (mainActivityViewModel.showUpdateDialog.collectAsState().value)
                {
                    UpdateRequiredDialog()
                }
            }
        }
    }


    @Composable
    fun UpdateRequiredDialog()
    {

        UpdateDialog(
            title = stringResource(id = R.string.update_required),
            text = stringResource(R.string.update_required_text),
            desc = stringResource(id = R.string.update_required_why),
            destText = stringResource(id = R.string.update_required_why_text),
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
                                         ),
            buttonClose = stringResource(id = R.string.exit)
                    )
    }

    @Composable
    fun UpdateProposalDialog(applicationVersion: ApplicationVersion)
    {
        UpdateDialog(
            title = stringResource(id = R.string.update_proposal),
            text = stringResource(R.string.update_proposal_text),
            desc = stringResource(id = R.string.update_proposal_what_new),
            destText = applicationVersion.desc,
            properties = DialogProperties(usePlatformDefaultWidth = false),
            buttonClose = stringResource(id = R.string.exit)
                    )
    }

    @Composable
    fun UpdateDialog(
        title: String,
        text: String,
        desc: String,
        destText: String,
        properties: DialogProperties,
        buttonClose: String,
                    )
    {
        var descVisible by rememberSaveable {
            mutableStateOf(false)
        }

        var dialogVisible by rememberSaveable {
            mutableStateOf(true)
        }

        if (dialogVisible)
        {
            Dialog(
                onDismissRequest = { }, properties = properties
                  ) {
                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                          ) {

                        Text(
                            text = title,
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center,
                            )
                        Spacer(modifier = Modifier.height(10.dp))

                        AnimatedContent(targetState = descVisible) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                  ) {
                                if (!it)
                                {
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.body1,
                                        textAlign = TextAlign.Center,
                                        )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    ClickableText(
                                        style = MaterialTheme.typography.body1.copy(
                                            color = MaterialTheme.colors.primary
                                                                                   ),
                                        text = AnnotatedString(desc)
                                                 ) {
                                        descVisible = true
                                    }
                                } else
                                {
                                    Text(
                                        text = destText,
                                        style = MaterialTheme.typography.body1,
                                        textAlign = TextAlign.Center,
                                        )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    ClickableText(
                                        style = MaterialTheme.typography.body1.copy(
                                            color = MaterialTheme.colors.primary
                                                                                   ),
                                        text = AnnotatedString(stringResource(id = R.string.back))
                                                 ) {
                                        descVisible = false
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row {
                            TextButton(modifier = Modifier.weight(1f), onClick = {
                                mainActivityViewModel.handle(
                                    MainActivityEvent.ExitClicked(this@MainActivity)
                                                            )
                                dialogVisible = false
                            }) {
                                Text(text = buttonClose)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    mainActivityViewModel.handle(
                                        MainActivityEvent.UpdateClicked(this@MainActivity)
                                                                )
                                },
                                  ) {
                                Text(
                                    text = stringResource(id = R.string.update),
                                    color = MaterialTheme.colors.onPrimary
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}