package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.util.DataState

@ExperimentalFoundationApi
@Composable
fun CommonReceiptsPage(
    viewModel: PaidServicesViewModel,
    emptyErrorMsg: String,
    receipts: @Composable (PaidServicesViewModel) -> DataState<List<Receipt>>
                      )
{
    ReceiptsPage(
        receipts = receipts(viewModel),
        complete = { it.date != null },
        emptyErrorMsg = emptyErrorMsg
                ) {
        ReceiptWidget(
            receipt = it, dateFormat = viewModel.dateFormat, modifier = Modifier.padding(5.dp)
                     )
    }
}

