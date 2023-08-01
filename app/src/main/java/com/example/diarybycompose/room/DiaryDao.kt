package com.example.diarybycompose.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diarybycompose.data.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM ItemEntity")
    fun getAllItem(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE id = :id")
    fun getItem(id: Int): Flow<ItemEntity>

    @Insert
    fun insertItem(item: ItemEntity)

    @Delete
    fun deleteItem(item: ItemEntity)

    @Update
    fun updateItem(item: ItemEntity)

}