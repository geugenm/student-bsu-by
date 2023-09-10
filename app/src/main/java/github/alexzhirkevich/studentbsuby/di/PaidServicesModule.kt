package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.data.models.PaidServicesInfo
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import github.alexzhirkevich.studentbsuby.repo.PaidServicesRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices.PaidServicesEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices.PaidServicesEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import javax.inject.Qualifier


@Qualifier
annotation class IsPaidInfoUpdatingQualifier

@Qualifier
annotation class HostelBillsQualifier

@Qualifier
annotation class PaidInfoBillsQualifier

@Qualifier
annotation class AcademDebtReceiptsQualifier

@Qualifier
annotation class CommonReceiptsQualifier

@Module
@InstallIn(ViewModelComponent::class)
class PaidServicesModule
{

    private val isUpdatingCommunication = StateFlowCommunication(false)

    private val paidInfoBillsCommunication = StateFlowCommunication<DataState<List<Bill>>>(
        DataState.Loading
                                                                                          )
    private val hostelBillsCommunication = StateFlowCommunication<DataState<List<Bill>>>(
        DataState.Loading
                                                                                        )

    private val academDebtReceipts = StateFlowCommunication<DataState<List<Receipt>>>(
        DataState.Loading
                                                                                     )
    private val commonReceipts = StateFlowCommunication<DataState<List<Receipt>>>(
        DataState.Loading
                                                                                 )

    private val paidInfoCommunication = StateFlowCommunication<DataState<PaidServicesInfo>>(
        DataState.Loading
                                                                                           )
    private val tutionFeeCommunication = StateFlowCommunication<DataState<List<TuitionFeePayment>>>(
        DataState.Loading
                                                                                                   )

    @Provides
    @IsPaidInfoUpdatingQualifier
    fun provideIsUpdatingCommunication(): StateCommunication<Boolean> = isUpdatingCommunication

    @Provides
    fun providePaidInfoCommunication(): StateCommunication<DataState<PaidServicesInfo>> =
        paidInfoCommunication

    @Provides
    @PaidInfoBillsQualifier
    fun providePaidInfoBillsCommunication(): StateCommunication<DataState<List<Bill>>> =
        paidInfoBillsCommunication

    @Provides
    @HostelBillsQualifier
    fun provideHostelBillsCommunication(): StateCommunication<DataState<List<Bill>>> =
        hostelBillsCommunication

    @Provides
    @AcademDebtReceiptsQualifier
    fun provideAcademDebtReceiptsCommunication(): StateCommunication<DataState<List<Receipt>>> =
        academDebtReceipts

    @Provides
    @CommonReceiptsQualifier
    fun provideCommonReceiptsCommunication(): StateCommunication<DataState<List<Receipt>>> =
        commonReceipts

    @Provides
    fun provideTutionFeeCommunication(): StateCommunication<DataState<List<TuitionFeePayment>>> =
        tutionFeeCommunication

    @Provides
    fun provideEventHandler(
        @ApplicationContext context: Context,
        paidServicesRepository: PaidServicesRepository,
        connectivityManager: ConnectivityManager,
                           ): SuspendEventHandler<PaidServicesEvent> = PaidServicesEventHandler(
        context = context,
        paidServicesRepository = paidServicesRepository,
        isUpdatingMapper = isUpdatingCommunication,
        paidInfoBillsMapper = paidInfoBillsCommunication,
        hostelBillsMapper = hostelBillsCommunication,
        academDebtReceiptsMapper = academDebtReceipts,
        commonReceiptsMapper = commonReceipts,
        paidInfoMapper = paidInfoCommunication,
        tutionFeePaymentsMapper = tutionFeeCommunication,
        connectivityManager = connectivityManager
                                                                                               )
}