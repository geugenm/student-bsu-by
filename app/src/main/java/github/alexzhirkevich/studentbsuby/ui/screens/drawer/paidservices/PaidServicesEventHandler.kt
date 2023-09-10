package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.data.models.PaidServicesInfo
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.PaidServicesRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import github.alexzhirkevich.studentbsuby.util.valueOrNull
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class PaidServicesEventHandler(
    private val context: Context,
    private val paidServicesRepository: PaidServicesRepository,
    private val connectivityManager: ConnectivityManager,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val paidInfoBillsMapper: StateMapper<DataState<List<Bill>>>,
    private val hostelBillsMapper: StateMapper<DataState<List<Bill>>>,
    private val academDebtReceiptsMapper: StateMapper<DataState<List<Receipt>>>,
    private val commonReceiptsMapper: StateMapper<DataState<List<Receipt>>>,
    private val tutionFeePaymentsMapper: StateMapper<DataState<List<TuitionFeePayment>>>,
    private val paidInfoMapper: StateMapper<DataState<PaidServicesInfo>>
                              ) :
    SuspendEventHandler<PaidServicesEvent> by SuspendEventHandler.from(
        UpdateRequestedHandler(
            paidServicesRepository = paidServicesRepository,
            connectivityManager = connectivityManager,
            isUpdatingMapper = isUpdatingMapper,
            paidInfoBillsMapper = paidInfoBillsMapper,
            hostelBillsMapper = hostelBillsMapper,
            academDebtReceiptsMapper = academDebtReceiptsMapper,
            tutionFeePaymentsMapper = tutionFeePaymentsMapper,
            commonReceiptsMapper = commonReceiptsMapper,
            paidInfoMapper = paidInfoMapper
                              ), EripHelpClickedHandler(context, paidServicesRepository)
                                                                      )

private class UpdateRequestedHandler(
    private val paidServicesRepository: PaidServicesRepository,
    private val connectivityManager: ConnectivityManager,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val paidInfoBillsMapper: StateMapper<DataState<List<Bill>>>,
    private val hostelBillsMapper: StateMapper<DataState<List<Bill>>>,
    private val academDebtReceiptsMapper: StateMapper<DataState<List<Receipt>>>,
    private val tutionFeePaymentsMapper: StateMapper<DataState<List<TuitionFeePayment>>>,
    private val commonReceiptsMapper: StateMapper<DataState<List<Receipt>>>,
    private val paidInfoMapper: StateMapper<DataState<PaidServicesInfo>>
                                    ) : BaseSuspendEventHandler<PaidServicesEvent.UpdateRequested>(
    PaidServicesEvent.UpdateRequested::class
                                                                                                  )
{

    override suspend fun launch()
    {
        isUpdatingMapper.map(false)
        paidInfoBillsMapper.map(DataState.Loading)
        hostelBillsMapper.map(DataState.Loading)
        academDebtReceiptsMapper.map(DataState.Loading)
        tutionFeePaymentsMapper.map(DataState.Loading)
        commonReceiptsMapper.map(DataState.Loading)
        paidInfoMapper.map(DataState.Loading)
        update(DataSource.All)
        connectivityManager.isNetworkConnected.collect {
            if (it)
            {
                update(DataSource.Remote)
            }
        }
    }


    override suspend fun handle(event: PaidServicesEvent.UpdateRequested)
    {
        isUpdatingMapper.map(true)
        update(DataSource.Remote)
        isUpdatingMapper.map(false)
    }

    private suspend fun update(source: DataSource)
    {
        paidServicesRepository.getInfoAndBills(source).process({
                DataState.Success(
                    paidInfoMapper.current.valueOrNull() to paidInfoBillsMapper.current.valueOrNull()
                                 )
            }, {
                if (it is DataState.Success)
                {
                    paidInfoMapper.map(it.value.first?.let {
                        DataState.Success(it)
                    } ?: DataState.Error(R.string.error_load_paidinfo))

                    paidInfoBillsMapper.map(it.value.second?.let {
                        if (it.isEmpty()) DataState.Empty
                        else DataState.Success(it)
                    } ?: DataState.Error(R.string.error_load_paidinfo))
                }
            }, R.string.error_load_paidinfo
                                                              )
        paidServicesRepository.getHostelBills(source).process(
                hostelBillsMapper::current, {
                    hostelBillsMapper.map(
                        if (it.valueOrNull()?.isEmpty() == true) DataState.Empty else it
                                         )
                }, R.string.error_load_hostel_bills
                                                             )
        paidServicesRepository.getAcademDebtFeeReceipts(source).process(
                academDebtReceiptsMapper::current, {
                    academDebtReceiptsMapper.map(
                        if (it.valueOrNull()?.isEmpty() == true) DataState.Empty else it
                                                )
                }, R.string.error_load_academ_debt_receipts
                                                                       )
        paidServicesRepository.getCommonReceipts(source).process(
                commonReceiptsMapper::current, {
                    commonReceiptsMapper.map(
                        if (it.valueOrNull()?.isEmpty() == true) DataState.Empty else it
                                            )
                }, R.string.error_load_receipts
                                                                )
        paidServicesRepository.getTuitionFeeReceipts().process(
                tutionFeePaymentsMapper::current, {
                    tutionFeePaymentsMapper.map(
                        if (it.valueOrNull()?.isEmpty() == true) DataState.Empty else it
                                               )
                }, R.string.error_load_tuition_fees
                                                              )
    }

    private suspend fun <T> Flow<T>.process(
        get: () -> DataState<T>, set: (DataState<T>) -> Unit, @StringRes errorMsg: Int
                                           ) = coroutineScope {
        launch {
            onEach {
                set(DataState.Success(it))
            }.onEmpty {
                set(DataState.Empty)
            }.catch {
                if (get() !is DataState.Success)
                {
                    set(DataState.Error(errorMsg, it))
                }
            }.collect()
        }
    }
}

private class EripHelpClickedHandler(
    private val context: Context, private val paidServicesRepository: PaidServicesRepository
                                    ) : BaseSuspendEventHandler<PaidServicesEvent.EripHelpClicked>(
    PaidServicesEvent.EripHelpClicked::class
                                                                                                  )
{
    override suspend fun handle(event: PaidServicesEvent.EripHelpClicked)
    {
        val uri = Uri.parse(paidServicesRepository.eripUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        context.startActivity(intent)
    }
}