package github.alexzhirkevich.studentbsuby.ui.screens.drawer.news

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar
import github.alexzhirkevich.studentbsuby.ui.theme.values.Colors

@Composable
fun NewsDetailsScreen(
    viewModel: NewsViewModel = hiltViewModel(), id: Int
) {

    val bgColor = Colors.GrayBackground

    Box(
        Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
    ) {

        BsuProgressBar(
            tint = MaterialTheme.colors.primary,
            size = 100.dp,
            modifier = Modifier.align(Alignment.Center)
        )


        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .navigationBarsWithImePadding(),
            factory = {
                WebView(it).apply {
                    setBackgroundColor(bgColor.toArgb())
                    viewModel.handle(NewsEvent.SetupWebView(id, this@apply))
                }
            })
    }
}