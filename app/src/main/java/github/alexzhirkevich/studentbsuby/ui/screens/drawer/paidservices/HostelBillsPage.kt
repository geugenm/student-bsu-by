package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import java.text.DateFormat
import java.util.Date

@Composable
fun HostelBillsPage(viewModel: PaidServicesViewModel)
{
    val bills by viewModel.hostelBillsCommunication.collectAsState()

    when (val b = bills)
    {
        is DataState.Success -> SuccessHostelBillsPage(
            bills = b.value, dateFormat = viewModel.dateFormat
                                                      )

        is DataState.Loading -> PaidServicesLoadingPage()
        is DataState.Error   -> PaidServicesErrorPage(
            title = stringResource(id = R.string.something_gone_wrong),
            error = stringResource(id = b.message)
                                                     )

        is DataState.Empty   -> PaidServicesErrorPage(
            title = stringResource(id = R.string.empty),
            error = stringResource(id = R.string.hostel_bills_empty)
                                                     )
    }
}

@Composable
private fun SuccessHostelBillsPage(
    bills: List<Bill>, dateFormat: DateFormat
                                  )
{
    SelectionContainer {

        LazyColumn(
            Modifier.fillMaxSize()
                  ) {
            items(bills.size) {
                HostelBillWidget(
                    bill = bills[it], dateFormat = dateFormat, modifier = Modifier.padding(5.dp)
                                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                      )
            }
        }
    }
}

@Composable
private fun HostelBillWidget(
    bill: Bill, dateFormat: DateFormat, modifier: Modifier = Modifier
                            )
{
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondary,
        ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
           ) {
            ProvideTextStyle(value = MaterialTheme.typography.body1) {
                Text(text = dateFormat.format(Date(bill.deadline)))
                Text(text = bill.price.toString() + " BYN")
            }
        }
    }
}

