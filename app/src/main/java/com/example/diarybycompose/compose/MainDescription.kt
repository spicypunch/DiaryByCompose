package com.example.diarybycompose.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diarybycompose.R
import com.example.diarybycompose.data.ItemEntity


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDescription(navController: NavController) {
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
    Scaffold() {
        SetBox(list, modifier = Modifier.fillMaxSize(), navController) {
            navController.navigate("add")
        }
    }
}

@Composable
fun SetBox(
    list: List<ItemEntity>,
    modifier: Modifier,
    navController: NavController,
    onClicked: () -> Unit
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
        FloatingActionButton(
            onClick = {
                onClicked()
            },
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
fun MyDiaryList(diaryLists: List<ItemEntity>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        ) {
        items(diaryLists.size) { count ->
            GridItem(diaryLists = diaryLists, count = count) {
                Log.e("test", "test")
                navController.navigate("detail")
            }
        }
    }
}

@Composable
fun GridItem(
    diaryLists: List<ItemEntity>,
    count: Int,
    onClicked: () -> Unit
) {
    var isFavorite by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.clickable { onClicked },
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