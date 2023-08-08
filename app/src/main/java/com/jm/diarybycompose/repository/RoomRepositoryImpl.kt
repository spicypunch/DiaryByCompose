package com.jm.diarybycompose.repository

import com.jm.diarybycompose.data.ItemEntity
import com.jm.diarybycompose.room.DiaryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDao
) : RoomRepository {
    override fun getAllItem(): Flow<List<ItemEntity>> = diaryDao.getAllItem()
    override fun getItem(id: Int): Flow<ItemEntity> = diaryDao.getItem(id)

    override suspend fun insertItem(itemEntity: ItemEntity) = withContext(Dispatchers.IO) {
        diaryDao.insertItem(itemEntity)
    }

    override suspend fun deleteItem(itemEntity: ItemEntity) = withContext(Dispatchers.IO) {
        diaryDao.deleteItem(itemEntity)
    }

    override suspend fun deleteAllItem() = withContext(Dispatchers.IO) {
        diaryDao.deleteAllItem()
    }

    override suspend fun updateItem(itemEntity: ItemEntity) = withContext(Dispatchers.IO){
        diaryDao.updateItem(itemEntity)
    }
}