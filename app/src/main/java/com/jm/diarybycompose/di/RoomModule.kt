package com.jm.diarybycompose.di

import android.content.Context
import androidx.room.Room
import com.jm.diarybycompose.data.room.AppDatabase
import com.jm.diarybycompose.data.room.DiaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "db_history"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGetDiaryDao(database: AppDatabase): DiaryDao {
        return database.getDiaryDao()
    }
}