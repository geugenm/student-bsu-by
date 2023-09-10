package github.alexzhirkevich.studentbsuby.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.data.models.NewsContent

@Dao
interface NewsDao
{

    @Insert(onConflict = REPLACE)
    suspend fun insert(news: News)

    @Insert(onConflict = REPLACE)
    suspend fun insertContent(content: NewsContent)

    @Query("SELECT * FROM News ORDER BY id DESC")
    suspend fun getAll(): List<News>

    @Query("SELECT * FROM NewsContent WHERE id = :id LIMIT 1")
    suspend fun getContent(id: Int): NewsContent

    @Query("DELETE FROM NewsContent WHERE id = :id")
    suspend fun clearContent(id: Int)

    @Query("DELETE FROM NEWS")
    suspend fun clear()
}