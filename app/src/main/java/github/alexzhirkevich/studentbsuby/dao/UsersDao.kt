package github.alexzhirkevich.studentbsuby.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import github.alexzhirkevich.studentbsuby.data.models.User

@Dao
interface UsersDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM User WHERE username = :username limit 1")
    suspend fun get(username: String): User?
}