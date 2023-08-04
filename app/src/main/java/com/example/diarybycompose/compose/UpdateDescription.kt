package com.example.diarybycompose.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diarybycompose.data.ItemEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDescription(
    navController: NavController,
    itemJsonString: String,
    onClicked: (ItemEntity) -> Unit
) {
    val gson = Gson()
    val itemType = object : TypeToken<ItemEntity>() {}.type
    val itemEntity: ItemEntity = gson.fromJson(itemJsonString, itemType)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val (title, setTitle) = rememberSaveable {
        mutableStateOf(itemEntity.title)
    }
    val (content, setContent) = rememberSaveable {
        mutableStateOf(itemEntity.content)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "일기 수정") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier.clickable { navController.popBackStack() })
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = setTitle,
                    label = { Text(text = "제목") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp)
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = setContent,
                    label = { Text(text = "내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("이미지 기능 추가 예정")
                        }
                    }) {
                    Text(text = "사 진\n추 가")
                }
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        onClicked(
                            ItemEntity(
                                id = itemEntity.id,
                                image = itemEntity.image,
                                title = title,
                                content = content,
                                date = itemEntity.date
                            )
                        )
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("빈칸을 채워주세요") }
                    }
                }
            ) {
                Text(text = "수정")

            }
        }
    }
}