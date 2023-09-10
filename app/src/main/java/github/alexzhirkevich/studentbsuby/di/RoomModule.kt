package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.alexzhirkevich.studentbsuby.dao.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule
{

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "db_cache")
            .fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideUsersDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideSubjectsDao(appDatabase: AppDatabase) = appDatabase.subjectsDao()

    @Singleton
    @Provides
    fun provideLessonsDao(appDatabase: AppDatabase) = appDatabase.lessonsDao()

    @Singleton
    @Provides
    fun provideHostelDao(appDatabase: AppDatabase) = appDatabase.hostelDao()

    @Singleton
    @Provides
    fun providePaidServicesDao(appDatabase: AppDatabase) = appDatabase.paidServicesDao()

    @Singleton
    @Provides
    fun provideNewsDao(appDatabase: AppDatabase) = appDatabase.newsDao()
}