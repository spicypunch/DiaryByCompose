package com.jm.diarybycompose.compose

import android.annotation.SuppressLint
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jm.diarybycompose.MainViewModel
import com.jm.diarybycompose.data.ItemEntity
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDescription(
    navController: NavController,
    allItem: List<ItemEntity>,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.insertResult.collectLatest {
            if (it == true) {
                snackbarHostState.showSnackbar("일기가 등록되었습니다.")
            } else {
                snackbarHostState.showSnackbar("등록에 실패하였습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteResult.collectLatest {
            if (it == true) {
                snackbarHostState.showSnackbar("일기가 삭제되었습니다.")
            } else {
                snackbarHostState.showSnackbar("삭제에 실패하였습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getItem.collectLatest {
            if (!it) {
                snackbarHostState.showSnackbar("일기를 가져오는데 실패하였습니다.")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) {
        SetBox(allItem, modifier = Modifier.fillMaxSize(), navController) {
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
    var isFavorite by rememberSaveable {
        mutableStateOf(false)
    }

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
                    painter = painterResource(R.drawable.basic),
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