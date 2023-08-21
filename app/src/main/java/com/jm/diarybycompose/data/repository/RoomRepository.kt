package com.jm.diarybycompose.data.repository

import com.jm.diarybycompose.data.domain.model.ItemEntity
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun getAllItem(): Flow<List<ItemEntity>>

    fun getItem(id: Int): Flow<ItemEntity>

    fun getLikeItem(): Flow<List<ItemEntity>>

    suspend fun insertItem(itemEntity: ItemEntity)

    suspend fun deleteItem(itemEntity: ItemEntity)

    suspend fun deleteAllItem()

    suspend fun updateItem(itemEntity: ItemEntity)
}