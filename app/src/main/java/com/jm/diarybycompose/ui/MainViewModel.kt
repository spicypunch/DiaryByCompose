package com.jm.diarybycompose.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.data.repository.RoomRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomRepository: RoomRepositoryImpl
) : ViewModel() {

    private val _insertResult = MutableSharedFlow<Boolean>()
    val insertResult = _insertResult.asSharedFlow()

    private val _deleteResult = MutableSharedFlow<Boolean>()
    val deleteResult = _deleteResult.asSharedFlow()

    private val _getItem = MutableSharedFlow<Boolean>()
    val getItem = _getItem.asSharedFlow()

    private val _allItem = mutableStateOf(emptyList<ItemEntity>())
    val allItem: State<List<ItemEntity>> = _allItem

    private var _item = mutableStateOf<ItemEntity?>(null)
    val item: State<ItemEntity?> = _item

    private val _likeItem = mutableStateOf(emptyList<ItemEntity>())
    val likeItem: State<List<ItemEntity>> = _likeItem

    fun insertItem(title: String, content: String, imageUri: Uri?, date: String) {
        viewModelScope.launch {
            try {
                roomRepository.insertItem(
                    ItemEntity(
                        title = title,
                        content = content,
                        imageUri = imageUri.toString(),
                        date = date,
                        like = false
                    )
                )
                _insertResult.emit(true)
            } catch (e: Exception) {
                Log.e("InsertItemErr", e.toString())
                _insertResult.emit(false)
            }
        }
    }

    fun getAllItem() {
        viewModelScope.launch {
            try {
                roomRepository.getAllItem().collect() { result ->
                    _allItem.value = result
                }
            } catch (e: Exception) {
                _getItem.emit(false)
                Log.e("GetAllItemErr", e.toString())
            }
        }
    }

    fun getItem(id: Int) {
        viewModelScope.launch {
            try {
                roomRepository.getItem(id).collect() { result ->
                    _item.value = result
                }
            } catch (e: Exception) {
                Log.e("GetItemErr", e.toString())
            }
        }
    }

    fun getLikeItem() {
        viewModelScope.launch {
            try {
                roomRepository.getLikeItem().collect() { result ->
                    _likeItem.value = result
                }
            } catch (e: Exception) {
                Log.e("GetLikeItemErr", e.toString())
            }
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            try {
                roomRepository.deleteItem(item)
                _deleteResult.emit(true)
            } catch (e: Exception) {
                _deleteResult.emit(false)
                Log.e("DeleteItemErr", e.toString())
            }
        }
    }

    fun deleteAllItem() {
        viewModelScope.launch {
            try {
                roomRepository.deleteAllItem()
                _deleteResult.emit(true)
            } catch (e: Exception) {
                _deleteResult.emit(false)
                Log.e("DeleteAllItemErr", e.toString())
            }
        }
    }

    fun updateItem(item: ItemEntity) {
        viewModelScope.launch {
            try {
                roomRepository.updateItem(item)
            } catch (e: Exception) {
                Log.e("UpdateItemErr", e.toString())
            }
        }
    }
}