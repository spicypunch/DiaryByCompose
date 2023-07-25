package com.example.diarybycompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diarybycompose.data.ItemEntity
import com.example.diarybycompose.ui.theme.DiaryByComposeTheme

@Composable
fun MainDescription(modifier: Modifier = Modifier) {
    MyDiaryList(items = ItemEntity(title = "안녕", content = "하세요"))
}

@Composable
fun MyDiaryList(items: ItemEntity, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(20) { item ->
            GridItem(item)
        }
    }
}

@Composable
fun GridItem(item: Int) {
    Box() {
        Text(
            text =  item.toString(),
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyDiaryListPreview() {
    DiaryByComposeTheme {
        MyDiaryList(items = ItemEntity(title = "안녕", content = "하세요"))
    }
}