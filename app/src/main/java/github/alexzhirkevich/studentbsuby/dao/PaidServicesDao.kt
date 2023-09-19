package github.alexzhirkevich.studentbsuby.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import github.alexzhirkevich.studentbsuby.data.models.*

@Dao
interface PaidServicesDao
{

    @Insert(onConflict = REPLACE)
    suspend fun insertInfo(info: PaidServicesInfo)

    @Insert(onConflict = REPLACE)
    suspend fun insertTuitionFeeReceipt(payment: TuitionFeePayment)

    @Insert(onConflict = REPLACE)
    suspend fun insertReceipt(receipt: Receipt)

    @Insert(onConflict = REPLACE)
    suspend fun insertBill(bill: Bill)

    @Query("SELECT * FROM PaidServicesInfo WHERE owner = :username LIMIT 1")
    suspend fun getInfo(username: String): PaidServicesInfo?

    @Query("SELECT * FROM Bill WHERE owner = :username AND billType = :type")
    suspend fun getBills(username: String, type: Int): List<Bill>

    @Query("SELECT * FROM TuitionFeePayment WHERE owner = :username ORDER BY deadline DESC")
    suspend fun getTuitionFeeReceipts(username: String): List<TuitionFeePayment>

    @Query("SELECT * FROM Receipt WHERE owner = :username AND receiptType = :type ORDER BY deadline DESC")
    suspend fun getReceipts(username: String, type: Int): List<Receipt>

    @Query("DELETE FROM TuitionFeePayment WHERE owner = :username")
    suspend fun clearTuitionFeeReceipts(username: String)

    @Query("DELETE FROM Receipt WHERE owner = :username AND receiptType = :type")
    suspend fun clearReceipts(username: String, type: Int)

    @Query("DELETE FROM Bill WHERE owner = :username AND billType = :type")
    suspend fun clearBills(username: String, type: Int)
}