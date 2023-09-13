package github.alexzhirkevich.studentbsuby.repo

import android.content.res.Resources
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.api.PaidServicesApi
import github.alexzhirkevich.studentbsuby.dao.PaidServicesDao
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.data.models.PaidServicesInfo
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PaidServicesRepository @Inject constructor(
    usernameProvider: UsernameProvider,
    api: PaidServicesApi,
    dao: PaidServicesDao,
    resources: Resources
                                                )
{

    private val dateFormat = SimpleDateFormat("dd.mm.yyyy", Locale.getDefault())

    private val infoAndBillsRepository: Repository<Pair<PaidServicesInfo, List<Bill>>> =
        InfoAndBillsRepository(usernameProvider, api, dao, dateFormat)

    private val tuitionFeeRepository: Repository<List<TuitionFeePayment>> =
        TuitionFeeReceiptsRepository(usernameProvider, api, dao, dateFormat)

    private val academDebtRepository: Repository<List<Receipt>> =
        AcademDebtRepository(usernameProvider, api, dao, dateFormat)

    private val hostelBillsRepository: Repository<List<Bill>> =
        HostelBillsRepository(usernameProvider, api, dao, dateFormat, resources)

    private val commonReceiptsRepository: Repository<List<Receipt>> =
        CommonReceiptsRepository(usernameProvider, api, dao, dateFormat)

    val eripUrl get() = "https://student.bsu.by/PersonalCabinet/Pay/docs/%D0%98%D0%BD%D1%81%D1%82%D1%80%D1%83%D0%BA%D1%86%D0%B8%D1%8F%20%D0%BE%20%D0%BF%D1%80%D0%BE%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B8%20%D0%BE%D0%BF%D0%BB%D0%B0%D1%82%D1%8B%20%D1%87%D0%B5%D1%80%D0%B5%D0%B7%20%D0%90%D0%98%D0%A1%20%D0%95%D0%A0%D0%98%D0%9F.pdf"

    fun getInfoAndBills(source: DataSource = DataSource.All): Flow<Pair<PaidServicesInfo, List<Bill>>> =
        infoAndBillsRepository.get(source)

    fun getTuitionFeeReceipts(source: DataSource = DataSource.All): Flow<List<TuitionFeePayment>> =
        tuitionFeeRepository.get(source).map { it.sortedByDescending(TuitionFeePayment::deadline) }

    fun getAcademDebtFeeReceipts(source: DataSource = DataSource.All): Flow<List<Receipt>> =
        academDebtRepository.get(source).map { it.sortedByDescending(Receipt::deadline) }

    fun getHostelBills(source: DataSource = DataSource.All): Flow<List<Bill>> =
        hostelBillsRepository.get(source).map { it.sortedByDescending(Bill::deadline) }

    fun getCommonReceipts(source: DataSource = DataSource.All): Flow<List<Receipt>> =
        commonReceiptsRepository.get(source).map { it.sortedByDescending(Receipt::deadline) }
}

private class CommonReceiptsRepository(
    private val usernameProvider: UsernameProvider,
    private val api: PaidServicesApi,
    private val dao: PaidServicesDao,
    private val dateFormat: DateFormat,
                                      ) : CacheWebRepository<List<Receipt>>()
{

    override suspend fun getFromCache(): List<Receipt>? = kotlin.runCatching {
        dao.getReceipts(usernameProvider.username, Receipt.TYPE_COMMON)
    }.getOrNull()

    override suspend fun getFromWeb(): List<Receipt>?
    {
        return Jsoup.parse(api.common().html()).getElementsByTag("tbody").lastOrNull()
            ?.getElementsByTag("tr")?.toList()?.drop(1)?.dropLast(1)?.mapNotNull { elem ->
                kotlin.runCatching {
                    elem.getElementsByTag("td").takeIf { it.size == 5 }?.mapNotNull(Element::text)
                        ?.let {
                            Receipt(
                                owner = usernameProvider.username,
                                deadline = dateFormat.parse(it[0])!!.time,
                                price = it[1].toFloatOrNull(),
                                left = it[2].toFloatOrNull(),
                                date = dateFormat.parse(it[3])?.time,
                                info = it[4],
                                receiptType = Receipt.TYPE_COMMON,
                                   )
                        }
                }.getOrNull()
            }
    }

    override suspend fun saveToCache(value: List<Receipt>)
    {
        kotlin.runCatching {
            dao.clearReceipts(usernameProvider.username, Receipt.TYPE_COMMON)
            value.forEach { dao.insertReceipt(it) }
        }
    }

}

private class HostelBillsRepository(
    private val usernameProvider: UsernameProvider,
    private val api: PaidServicesApi,
    private val dao: PaidServicesDao,
    private val dateFormat: DateFormat,
    private val resources: Resources
                                   ) : CacheWebRepository<List<Bill>>()
{

    override suspend fun getFromCache(): List<Bill>? = kotlin.runCatching {
        dao.getBills(usernameProvider.username, Bill.TYPE_HOSTEL)
    }.getOrNull()

    override suspend fun getFromWeb(): List<Bill>?
    {
        return Jsoup.parse(api.hostelBills().html()).getElementsByTag("tbody").lastOrNull()
            ?.getElementsByTag("tr")?.toList()?.drop(1)?.dropLast(1)?.mapNotNull { elem ->
                kotlin.runCatching {
                    elem.getElementsByTag("td").takeIf { it.size == 2 }?.mapNotNull(Element::text)
                        ?.let {
                            Bill(
                                owner = usernameProvider.username,
                                deadline = dateFormat.parse(it[0])!!.time,
                                price = it[1].toFloat(),
                                billType = Bill.TYPE_HOSTEL,
                                paymentType = resources.getString(R.string.hostel_payment)
                                )
                        }
                }.getOrNull()
            }
    }

    override suspend fun saveToCache(value: List<Bill>)
    {
        kotlin.runCatching {
            dao.clearBills(usernameProvider.username, Bill.TYPE_HOSTEL)
            value.forEach { dao.insertBill(it) }
        }
    }
}

private class AcademDebtRepository(
    private val usernameProvider: UsernameProvider,
    private val api: PaidServicesApi,
    private val dao: PaidServicesDao,
    private val dateFormat: DateFormat
                                  ) : CacheWebRepository<List<Receipt>>()
{

    override suspend fun getFromCache(): List<Receipt>? = kotlin.runCatching {
        dao.getReceipts(usernameProvider.username, Receipt.TYPE_ACADEM_DEBT)
    }.getOrNull()

    override suspend fun getFromWeb(): List<Receipt>?
    {
        return Jsoup.parse(api.academicDebt().html()).getElementsByTag("tbody").lastOrNull()
            ?.getElementsByTag("tr")?.toList()?.drop(1)?.dropLast(1)?.mapNotNull { elem ->
                kotlin.runCatching {
                    elem.getElementsByTag("td").takeIf { it.size == 5 }?.mapNotNull(Element::text)
                        ?.let {
                            Receipt(owner = usernameProvider.username,
                                deadline = dateFormat.parse(it[0])!!.time,
                                price = it[1].toFloatOrNull(),
                                left = it[2].toFloatOrNull(),
                                date = kotlin.runCatching { dateFormat.parse(it[3])?.time }
                                    .getOrNull(),
                                info = it[4],
                                receiptType = Receipt.TYPE_ACADEM_DEBT)
                        }
                }.getOrNull()
            }
    }

    override suspend fun saveToCache(value: List<Receipt>)
    {
        kotlin.runCatching {
            dao.clearReceipts(usernameProvider.username, Receipt.TYPE_ACADEM_DEBT)
            value.forEach { dao.insertReceipt(it) }
        }
    }
}

private class InfoAndBillsRepository(
    private val usernameProvider: UsernameProvider,
    private val api: PaidServicesApi,
    private val dao: PaidServicesDao,
    private val dateFormat: DateFormat
                                    ) : CacheWebRepository<Pair<PaidServicesInfo, List<Bill>>>()
{

    override suspend fun getFromCache(): Pair<PaidServicesInfo, List<Bill>>? = kotlin.runCatching {
        dao.getInfo(usernameProvider.username)?.let {
            it to dao.getBills(usernameProvider.username, Bill.TYPE_PAIDINFO)
        }
    }.getOrNull()

    override suspend fun getFromWeb(): Pair<PaidServicesInfo, List<Bill>>?
    {

        val jsoup = Jsoup.parse(api.info().html())

        val contNum =
            jsoup.getElementById("ctl00_ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ContentPlaceHolder1_lbDogNum")
                ?.text()?.split(" ")?.lastOrNull() ?: return null

        val debt =
            jsoup.getElementById("ctl00_ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ContentPlaceHolder1_lDolg")
                ?.text()?.toFloatOrNull() ?: return null

        val fine =
            jsoup.getElementById("ctl00_ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ContentPlaceHolder1_lPeny")
                ?.text()?.toFloatOrNull() ?: return null

        val price =
            jsoup.getElementById("ctl00_ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ContentPlaceHolder1_lPrice")
                ?.text()?.toFloatOrNull()

        val billsData =
            jsoup.getElementById("ctl00_ctl00_ctl00_ContentPlaceHolder0_ContentPlaceHolder1_ContentPlaceHolder1_gv")
                ?.getElementsByAttributeValue("valign", "middle")?.filterNot { it.hasAttr("scope") }
                ?.map { it.text() }?.chunked(3)

        val billsAndTotal = billsData?.take(billsData.size - 1)?.mapNotNull {
            if (it.size == 3) Bill(
                owner = usernameProvider.username,
                paymentType = it[0],
                deadline = dateFormat.parse(it[1])!!.time,
                price = it[2].toFloatOrNull() ?: 0f,
                billType = Bill.TYPE_PAIDINFO
                                  )
            else null
        }
        val bills = billsAndTotal?.subList(
            fromIndex = 0, toIndex = billsAndTotal.lastIndex - 1
                                          )


        return PaidServicesInfo(
            owner = usernameProvider.username,
            contractNumber = contNum,
            debt = debt,
            fine = fine,
            price = price,
                               ) to bills.orEmpty()
    }

    override suspend fun saveToCache(value: Pair<PaidServicesInfo, List<Bill>>)
    {
        kotlin.runCatching {
            dao.insertInfo(value.first)
            dao.clearBills(value.second[0].owner, Bill.TYPE_PAIDINFO)
            value.second.forEach { dao.insertBill(it) }
        }
    }
}

private class TuitionFeeReceiptsRepository(
    private val usernameProvider: UsernameProvider,
    private val api: PaidServicesApi,
    private val dao: PaidServicesDao,
    private val dateFormat: DateFormat
                                          ) : CacheWebRepository<List<TuitionFeePayment>>()
{

    override suspend fun getFromCache(): List<TuitionFeePayment>? = kotlin.runCatching {
        dao.getTuitionFeeReceipts(usernameProvider.username)
    }.getOrNull()

    override suspend fun getFromWeb(): List<TuitionFeePayment>? =
        Jsoup.parse(api.tuitionFee().html()).getElementsByTag("tbody").lastOrNull()
            ?.getElementsByTag("tr")?.toList()?.drop(2)?.dropLast(1)?.mapNotNull { elem ->
                kotlin.runCatching {
                    elem.getElementsByTag("td").takeIf { it.size == 9 }?.mapNotNull(Element::text)
                        ?.let {
                            TuitionFeePayment(owner = usernameProvider.username,
                                year = it[0],
                                deadline = dateFormat.parse(it[1])!!.time,
                                fullPrice = it[2].toFloat(),
                                date = kotlin.runCatching { dateFormat.parse(it[3])?.time }
                                    .getOrNull(),
                                price = it[4].toFloatOrNull(),
                                left = it[5].toFloatOrNull(),
                                fineDays = it[6].toIntOrNull() ?: 0,
                                fineSize = it[7].toFloatOrNull() ?: 0f,
                                debt = it[8].toFloatOrNull())
                        }
                }.getOrNull()
            }

    override suspend fun saveToCache(value: List<TuitionFeePayment>)
    {
        kotlin.runCatching {
            dao.clearTuitionFeeReceipts(usernameProvider.username)
        }
    }
}
