package com.jm.diarybycompose.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jm.diarybycompose.data.domain.model.ItemEntity

@Database(entities = [ItemEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}