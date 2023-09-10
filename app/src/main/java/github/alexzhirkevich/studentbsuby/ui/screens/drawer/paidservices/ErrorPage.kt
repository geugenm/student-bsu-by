package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.ui.common.ErrorWidget

@Composable
fun PaidServicesErrorPage(
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
