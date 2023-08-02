package com.example.diarybycompose.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diarybycompose.data.ItemEntity

@Database(entities = [ItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}