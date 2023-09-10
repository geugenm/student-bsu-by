package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.data.models.PaidServicesInfo
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import github.alexzhirkevich.studentbsuby.di.AcademDebtReceiptsQualifier
import github.alexzhirkevich.studentbsuby.di.CommonReceiptsQualifier
import github.alexzhirkevich.studentbsuby.di.HostelBillsQualifier
import github.alexzhirkevich.studentbsuby.di.IsPaidInfoUpdatingQualifier
import github.alexzhirkevich.studentbsuby.di.PaidInfoBillsQualifier
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.ErrorHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendHandlerViewModel
import github.alexzhirkevich.studentbsuby.util.Updatable
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaidServicesViewModel @Inject constructor(
    @IsPaidInfoUpdatingQualifier override val isUpdating: StateCommunication<Boolean>,
    val paidInfoCommunication: StateCommunication<DataState<PaidServicesInfo>>,
    @PaidInfoBillsQualifier
    val paidInfoBillsCommunication: StateCommunication<DataState<List<Bill>>>,
    @HostelBillsQualifier val hostelBillsCommunication: StateCommunication<DataState<List<Bill>>>,
    @AcademDebtReceiptsQualifier
    val academDebtReceiptsCommunication: StateCommunication<DataState<List<Receipt>>>,
    @CommonReceiptsQualifier
    val commonReceiptsCommunication: StateCommunication<DataState<List<Receipt>>>,
    val tutionFeeCommunication: StateCommunication<DataState<List<TuitionFeePayment>>>,
    eventHandler: SuspendEventHandler<PaidServicesEvent>,
    errorHandler: ErrorHandler,
    dispatchers: Dispatchers,
                                               ) : SuspendHandlerViewModel<PaidServicesEvent>(
    dispatchers = dispatchers, errorHandler = errorHandler, suspendEventHandler = eventHandler
                                                                                             ),
    Updatable
{

    val dateFormat: DateFormat = SimpleDateFormat("dd.mm.yyy", Locale.getDefault())


    override fun update()
    {
        handle(PaidServicesEvent.UpdateRequested)
    }

}