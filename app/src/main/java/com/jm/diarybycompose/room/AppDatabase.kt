package com.jm.diarybycompose.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jm.diarybycompose.data.ItemEntity

@Database(entities = [ItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}