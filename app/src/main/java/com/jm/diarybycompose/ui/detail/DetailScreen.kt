package com.jm.diarybycompose.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.jm.diarybycompose.R
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.ui.MainViewModel
import com.jm.diarybycompose.ui.dialog.RemoveDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Int,
    viewModel: MainViewModel = hiltViewModel(),
    callNavController: () -> Unit,
    onClicked: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.getItem(id)
    val itemEntity: ItemEntity? = viewModel.item.value
    val gson = Gson()
    val itemJsonString = gson.toJson(itemEntity)
    var openDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { callNavController() })
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = if (itemEntity?.imageUri != "null") itemEntity?.imageUri else R.drawable.round_menu_book_24),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.Start)
                        .align(Alignment.End),
                    contentScale = ContentScale.Inside
                )
                itemEntity?.let {
                    Text(text = itemEntity.title, fontSize = 35.sp)
                    Text(
                        text = itemEntity.date,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Text(
                        text = itemEntity.content,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onClicked(itemJsonString)
                    }
                ) {
                    Text(text = "수정하기")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        openDialog = true
                    }) {
                    Text(text = "삭제하기")
                }
            }
        }
    }
    if (openDialog) {
        RemoveDialog {
            if (it) {
                if (itemEntity != null) {
                    viewModel.deleteItem(itemEntity)
                }
                callNavController()
            }
            openDialog = false
        }
    }
}