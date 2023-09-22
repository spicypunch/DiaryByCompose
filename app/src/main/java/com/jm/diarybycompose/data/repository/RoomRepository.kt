package com.jm.diarybycompose.data.repository

import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.data.domain.model.NotificationStateEntity
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun getAllItem(): Flow<List<ItemEntity>>

    fun getItem(id: Int): Flow<ItemEntity>

    fun getLikeItem(): Flow<List<ItemEntity>>

    suspend fun insertItem(itemEntity: ItemEntity)

    suspend fun deleteItem(itemEntity: ItemEntity)

    suspend fun deleteAllItem()

    suspend fun updateItem(itemEntity: ItemEntity)

    fun searchItem(search: String): Flow<List<ItemEntity>>

    suspend fun insertNotificationState(stateEntity: NotificationStateEntity)

    suspend fun updateNotificationState(stateEntity: NotificationStateEntity)

    fun getNotificationState(): Flow<NotificationStateEntity>
}