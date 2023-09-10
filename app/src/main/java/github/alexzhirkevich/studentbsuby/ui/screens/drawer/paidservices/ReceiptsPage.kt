package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.util.DataState
import java.text.DateFormat
import java.util.Date

@ExperimentalFoundationApi
@Composable
fun <T> ReceiptsPage(
    receipts: DataState<List<T>>,
    header: ((T) -> String)? = null,
    complete: (T) -> Boolean,
    emptyErrorMsg: String,
    widget: @Composable (T) -> Unit
                    )
{

    when (receipts)
    {
        is DataState.Success -> SuccessReceiptsPage(
            receipts = receipts.value, header = header, complete = complete
                                                   ) {
            widget(it)
        }

        is DataState.Loading -> PaidServicesLoadingPage()
        is DataState.Error   -> PaidServicesErrorPage(
            title = stringResource(id = R.string.something_gone_wrong),
            error = stringResource(id = receipts.message)
                                                     )

        is DataState.Empty   -> PaidServicesErrorPage(
            title = stringResource(id = R.string.empty), error = emptyErrorMsg
                                                     )
    }
}

@Composable
fun ReceiptWidget(
    receipt: Receipt, dateFormat: DateFormat, modifier: Modifier = Modifier
                 )
{

    @Composable
    fun BillRow(name: String, value: String)
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
           ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.onSecondary)
                            ) {

                Text(text = "$name:")
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = value, textAlign = TextAlign.End
                    )
            }
        }
    }
    Card(
        elevation = 3.dp,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondary,

        ) {
        Column(Modifier.padding(10.dp)) {

            if (receipt.date != null && receipt.price != null)
            {
                Text(
                    text = dateFormat.format(Date(receipt.date)),
                    style = MaterialTheme.typography.body1
                    )
                BillRow(
                    name = stringResource(id = R.string.sum),
                    value = "%.2f".format(receipt.price) + " " + stringResource(id = R.string.currency)
                       )

                BillRow(
                    name = stringResource(id = R.string.deadline),
                    value = dateFormat.format(Date(receipt.deadline))
                       )
                BillRow(
                    name = stringResource(id = R.string.left),
                    value = "%.2f".format(receipt.left) + " " + stringResource(id = R.string.currency)
                       )
            } else
            {
                Text(
                    text = dateFormat.format(Date(receipt.deadline)),
                    style = MaterialTheme.typography.body1
                    )
                BillRow(
                    name = stringResource(id = R.string.left),
                    value = "%.2f".format(receipt.left) + " " + stringResource(id = R.string.currency)
                       )
            }
            receipt.info?.let {
                BillRow(
                    name = stringResource(id = R.string.additionally), value = it
                       )
            }
        }
    }
}