package com.example.diarybycompose

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarybycompose.data.ItemEntity
import com.example.diarybycompose.repository.RoomRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomRepository: RoomRepositoryImpl
) : ViewModel() {

    private val _insertResult = mutableStateOf<Boolean?>(null)
    val insertResult: State<Boolean?> = _insertResult

    private val _allItem = mutableStateOf<List<ItemEntity>?>(null)
    val allItem: State<List<ItemEntity>?> = _allItem

    private val _item = mutableStateOf<ItemEntity?>(null)
    val item: State<ItemEntity?> = _item
    fun insertItem(title: String, content: String) {
        viewModelScope.launch {
            try {
                roomRepository.insertItem(ItemEntity(title = title, content = content))
                _insertResult.value = true
            } catch (e: Exception) {
                Log.e("InsertItemErr", e.toString())
                _insertResult.value = false
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
}