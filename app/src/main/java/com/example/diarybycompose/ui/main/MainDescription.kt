package com.example.diarybycompose.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diarybycompose.R
import com.example.diarybycompose.data.ItemEntity
import com.example.diarybycompose.ui.theme.DiaryByComposeTheme


@Composable
fun MainDescription() {
    val list = mutableListOf(
        ItemEntity(title = "test1", content = "test1"),
        ItemEntity(title = "test2", content = "test2"),
        ItemEntity(title = "test3", content = "test3"),
        ItemEntity(title = "test4", content = "test4"),
        ItemEntity(title = "test5", content = "test5"),
        ItemEntity(title = "test6", content = "test6"),
        ItemEntity(title = "test7", content = "test7"),
        ItemEntity(title = "test8", content = "test8"),
        ItemEntity(title = "test9", content = "test9"),
        ItemEntity(title = "test10", content = "test10"),
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MyDiaryList(list)
        }
    }
}

@Composable
fun MyDiaryList(diaryLists: List<ItemEntity>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        contentPadding = PaddingValues(20.dp)
    ) {
        items(diaryLists.size) { count ->
            GridItem(diaryLists, count)
        }
    }
}

@Composable
fun GridItem(diaryLists: List<ItemEntity>, count: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "MyDiaryImage"
            )
            Text(
                text = diaryLists[count].title,
                color = Color.Black,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyDiaryListPreview() {
    DiaryByComposeTheme {
        val list = mutableListOf(
            ItemEntity(title = "test", content = "test"),
            ItemEntity(title = "test2", content = "test2"),
            ItemEntity(title = "test3", content = "test3")
        )
        MyDiaryList(list)
    }
}