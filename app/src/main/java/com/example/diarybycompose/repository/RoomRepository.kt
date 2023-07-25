package com.example.diarybycompose.repository

import com.example.diarybycompose.data.ItemEntity
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun getAllItem(): Flow<List<ItemEntity>>

    suspend fun insertItem(itemEntity: ItemEntity)

    suspend fun deleteItem(itemEntity: ItemEntity)

    suspend fun updateItem(itemEntity: ItemEntity)
}