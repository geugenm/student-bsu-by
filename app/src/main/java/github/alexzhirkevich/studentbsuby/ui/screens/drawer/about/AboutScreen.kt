package github.alexzhirkevich.studentbsuby.ui.screens.drawer.about

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsHeight
import de.charlex.compose.HtmlText
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.ui.common.NavigationMenuButton
import github.alexzhirkevich.studentbsuby.util.animatedSquaresBackground
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState


@ExperimentalToolbarApi
@Composable
fun AboutScreen(
    isTablet: Boolean,
    aboutViewModel: AboutViewModel = hiltViewModel(),
    onMenuClicked: () -> Unit
) {

    val scaffoldState = rememberCollapsingToolbarScaffoldState(
        toolbarState = rememberCollapsingToolbarState(0)
    )

    LaunchedEffect(Unit) {
        scaffoldState.toolbarState.collapse(0)
        scaffoldState.toolbarState.expand(500)
    }

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        toolbarModifier = Modifier
            .background(MaterialTheme.colors.secondary),
        state = scaffoldState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Column {
                Spacer(modifier = Modifier.statusBarsHeight())
                TopAppBar(
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                ) {

                    if (!isTablet) {
                        NavigationMenuButton(onClick = onMenuClicked)
                    }
                    AnimatedVisibility(visible = scaffoldState.toolbarState.progress == 0f) {
                        Text(
                            text = stringResource(id = R.string.about),
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }

            }

            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .parallax(0.5f)
                    .animatedSquaresBackground(
                        color = MaterialTheme.colors.primary.copy(alpha = .05f),
                        count = 10,
                        size = 200.dp
                    )
                    .alpha(scaffoldState.toolbarState.progress)
                    .padding(
                        vertical = 60.dp,
                        horizontal = 50.dp
                    )
            )
        }) {

        Column(
            Modifier
                .bsuBackgroundPattern(
                    color = MaterialTheme.colors.primary.copy(alpha = .05f)
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)


        ) {

            Card(
                elevation = 3.dp,
                backgroundColor = MaterialTheme.colors.secondary,
            ) {
                HtmlText(
                    textId = R.string.about_text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(10.dp),
                    urlSpanStyle = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

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
                        modifier = Modifier.size(32.dp)
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
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}