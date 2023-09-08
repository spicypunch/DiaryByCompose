package com.jm.diarybycompose.ui.home

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
import androidx.compose.foundation.lazy.grid.items
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
import coil.compose.rememberAsyncImagePainter
import com.jm.diarybycompose.R
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.ui.MainViewModel

@Composable
fun DiaryListScreen(
    list: List<ItemEntity>,
    viewModel: MainViewModel,
    callNavController: (Int) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MyDiaryList(list, viewModel, callNavController)
        }
    }
}

@Composable
fun MyDiaryList(
    diaryLists: List<ItemEntity>,
    viewModel: MainViewModel,
    callNavController: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
    ) {
        items(diaryLists) { item ->
            GridItem(item, viewModel) { id ->
                callNavController(id)
            }
        }
    }
}


@Composable
fun GridItem(
    item: ItemEntity,
    viewModel: MainViewModel,
    onClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier.clickable { item.id?.let { onClicked(it) } },
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
                    painter = rememberAsyncImagePainter(model = if (item.imageUri != "null") item.imageUri else R.drawable.round_menu_book_24),
                    contentDescription = "MyDiaryImage",
                    modifier = Modifier.size(230.dp),
                    contentScale = if (item.imageUri != "null") ContentScale.Crop else ContentScale.Fit
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = {
                        viewModel.updateItem(
                            ItemEntity(
                                id = item.id,
                                title = item.title,
                                content = item.content,
                                imageUri = item.imageUri,
                                date = item.date,
                                like = !item.like
                            )
                        )
                    }) {
                        Icon(
                            imageVector = if (!item.like) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                            contentDescription = "favorite",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
        Text(
            text = item.title,
            color = Color.Black,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}