package com.example.diarybycompose.compose

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.diarybycompose.MainViewModel
import com.example.diarybycompose.R
import com.example.diarybycompose.data.ItemEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDescription(
    navController: NavController,
    itemJsonString: String,
    viewModel: MainViewModel,
    onClicked: (String) -> Unit
) {
    val gson = Gson()
    val itemType = object : TypeToken<ItemEntity>() {}.type
    val itemEntity: ItemEntity = gson.fromJson(itemJsonString, itemType)
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier.clickable { navController.popBackStack() })
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
                    painter = painterResource(id = R.drawable.basic),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.Start)
                        .align(Alignment.End)
                )
                Text(text = itemEntity.title, fontSize = 35.sp)
                Text(text = itemEntity.date, fontSize = 13.sp, modifier = Modifier.align(Alignment.End))
                Text(
                    text = itemEntity.content,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
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
                        viewModel.deleteItem(itemEntity)
                        navController.popBackStack()
                    }) {
                    Text(text = "삭제하기")
                }
            }
        }
    }
}