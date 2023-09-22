package com.jm.diarybycompose.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.data.domain.model.NotificationStateEntity

@Database(entities = [ItemEntity::class, NotificationStateEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}