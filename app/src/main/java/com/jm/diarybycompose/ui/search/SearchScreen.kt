package com.jm.diarybycompose.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jm.diarybycompose.ui.MainViewModel
import com.jm.diarybycompose.ui.home.DiaryListScreen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val (search, setSearch) = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchResult = viewModel.searchResult.value
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .height(50.dp),
                value = search,
                onValueChange = setSearch,
                textStyle = TextStyle(fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    viewModel.searchItem(search)
                })
            )
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.searchItem(search)
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(end = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
            }
        }
        DiaryListScreen(list = searchResult, viewModel = viewModel, navController = navController)
    }
}