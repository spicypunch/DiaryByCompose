package com.jm.diarybycompose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.jm.diarybycompose.R
import com.jm.diarybycompose.data.ItemEntity

@Composable
fun DiaryListScreen(
    list: List<ItemEntity>,
    modifier: Modifier,
    navController: NavController,
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MyDiaryList(list, navController)
        }
    }
}

@Composable
fun MyDiaryList(diaryLists: List<ItemEntity>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
    ) {
        items(diaryLists.size) { count ->
            GridItem(diaryLists = diaryLists, count = count) { id ->
                navController.navigate("detail/$id")
            }
        }
    }
}

@Composable
fun GridItem(
    diaryLists: List<ItemEntity>,
    count: Int,
    onClicked: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier.clickable { diaryLists[count].id?.let { onClicked(it) } },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            Box() {
                Image(
                    painter = rememberImagePainter(data = if (diaryLists[count].imageUri != "null") diaryLists[count].imageUri else R.drawable.diary),
                    contentDescription = "MyDiaryImage",
                    modifier = Modifier.size(230.dp),
                    contentScale = if (diaryLists[count].imageUri != "null") ContentScale.Crop else ContentScale.Fit
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        mainViewModel.updateItem(
                            ItemEntity(
                                id = diaryLists[count].id,
                                title = diaryLists[count].title,
                                content = diaryLists[count].content,
                                imageUri = diaryLists[count].imageUri,
                                date = diaryLists[count].date,
                                like = !diaryLists[count].like
                            )
                        )
                    }) {
                        Icon(
                            imageVector = if (!diaryLists[count].like) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
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