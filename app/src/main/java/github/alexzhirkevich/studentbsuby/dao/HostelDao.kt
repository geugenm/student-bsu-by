package github.alexzhirkevich.studentbsuby.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import github.alexzhirkevich.studentbsuby.data.models.HostelAdvert

@Dao
interface HostelDao
{

    @Insert
    suspend fun insert(value: HostelAdvert)

    @Query("SELECT * FROM HostelAdvert")
    suspend fun getAll(): List<HostelAdvert>

    @Query("DELETE FROM HostelAdvert")
    suspend fun clear()
}