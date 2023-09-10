package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.ui.common.BsuProgressBar

@Composable
fun PaidServicesLoadingPage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BsuProgressBar(
            Modifier.align(Alignment.Center),
            size = 100.dp,
            tint = MaterialTheme.colors.primary
        )
    }
}