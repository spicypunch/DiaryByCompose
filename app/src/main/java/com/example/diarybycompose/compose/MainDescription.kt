package com.example.diarybycompose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diarybycompose.R
import com.example.diarybycompose.compose.theme.DiaryByComposeTheme
import com.example.diarybycompose.data.ItemEntity


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

    SetBox(list, modifier = Modifier.fillMaxSize())
}

@Composable
fun SetBox(list: List<ItemEntity>, modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MyDiaryList(list)
        }
        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
            )
        }
    }
}

@Composable
fun MyDiaryList(diaryLists: List<ItemEntity>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,

        ) {
        items(diaryLists.size) { count ->
            GridItem(diaryLists = diaryLists, count = count)
        }
    }
}

@Composable
fun GridItem(
    diaryLists: List<ItemEntity>,
    count: Int,
) {
    var isFavorite by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Box() {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "MyDiaryImage",
                    contentScale = ContentScale.Fit
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                    }) {
                        Icon(
                            imageVector = if (!isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                            contentDescription = "favorite",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
        Text(
            text = diaryLists[count].title,
            color = Color.Black,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
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