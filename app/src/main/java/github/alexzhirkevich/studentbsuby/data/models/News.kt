package github.alexzhirkevich.studentbsuby.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class News(
    @PrimaryKey val id: Int,
    val title: String,
    val preview: String?,
               )

@Entity
data class NewsContent(
    @PrimaryKey val id: Int,
    val content: String,
                      )