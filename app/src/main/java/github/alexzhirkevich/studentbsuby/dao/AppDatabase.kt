package github.alexzhirkevich.studentbsuby.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import github.alexzhirkevich.studentbsuby.data.models.Bill
import github.alexzhirkevich.studentbsuby.data.models.HostelAdvert
import github.alexzhirkevich.studentbsuby.data.models.Lesson
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.data.models.NewsContent
import github.alexzhirkevich.studentbsuby.data.models.PaidServicesInfo
import github.alexzhirkevich.studentbsuby.data.models.Receipt
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.data.models.TuitionFeePayment
import github.alexzhirkevich.studentbsuby.data.models.User

@Database(
    entities = [User::class, Subject::class, Lesson::class, HostelAdvert::class, PaidServicesInfo::class, Bill::class, TuitionFeePayment::class, Receipt::class, News::class, NewsContent::class],
    exportSchema = false,
    version = 1
         )
abstract class AppDatabase : RoomDatabase()
{
    abstract fun userDao(): UsersDao

    abstract fun subjectsDao(): SubjectsDao

    abstract fun lessonsDao(): LessonsDao

    abstract fun hostelDao(): HostelDao

    abstract fun paidServicesDao(): PaidServicesDao

    abstract fun newsDao(): NewsDao
}