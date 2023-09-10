package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import java.text.DateFormat
import java.util.Date

@ExperimentalFoundationApi
@Composable
fun TuitionFeePage(
    viewModel: PaidServicesViewModel
                  )
{
    ReceiptsPage(receipts = viewModel.tutionFeeCommunication.collectAsState().value,
        header = { it.year },
        complete = { it.date != null },
        emptyErrorMsg = stringResource(id = R.string.tuition_fees_empty)
                ) {
        TuitionFeeReceiptWidget(
            payment = it, dateFormat = viewModel.dateFormat, modifier = Modifier.padding(5.dp)
                               )
    }
}

@Composable
private fun TuitionFeeReceiptWidget(
    payment: TuitionFeePayment, dateFormat: DateFormat, modifier: Modifier = Modifier
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
                Text(text = value)
            }
        }
    }
    Card(
        elevation = 3.dp,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondary,
        ) {
        Column(Modifier.padding(10.dp)) {

            if (payment.date != null && payment.price != null)
            {
                Text(
                    text = dateFormat.format(Date(payment.date)),
                    style = MaterialTheme.typography.body1
                    )
                BillRow(
                    name = stringResource(id = R.string.sum),
                    value = "%.2f".format(payment.price) + " " + stringResource(id = R.string.currency)
                       )
                if (payment.fineDays != 0)
                {
                    BillRow(
                        name = stringResource(id = R.string.fine_days),
                        value = payment.fineDays.toString()
                           )
                    BillRow(
                        name = stringResource(id = R.string.fine_size),
                        value = "%.2f".format(payment.fineSize) + " " + stringResource(id = R.string.currency)
                           )
                }
                BillRow(
                    name = stringResource(id = R.string.deadline),
                    value = dateFormat.format(Date(payment.deadline))
                       )

                BillRow(
                    name = stringResource(id = R.string.full_price),
                    value = "%.2f".format(payment.fullPrice) + " " + stringResource(id = R.string.currency)
                       )

                BillRow(
                    name = stringResource(id = R.string.left),
                    value = "%.2f".format(payment.left) + " " + stringResource(id = R.string.currency)
                       )
            } else
            {
                Text(
                    text = dateFormat.format(Date(payment.deadline)),
                    style = MaterialTheme.typography.body1
                    )
                BillRow(
                    name = stringResource(id = R.string.left),
                    value = "%.2f".format(payment.left) + " " + stringResource(id = R.string.currency)
                       )
            }
        }
    }
}