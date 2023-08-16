package com.jm.diarybycompose.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TabRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.jm.diarybycompose.MainViewModel
import com.jm.diarybycompose.data.ItemEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

lateinit var mainViewModel: MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    allItem: List<ItemEntity>,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = listOf("일기", "❤️")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    mainViewModel = viewModel
    mainViewModel.getLikeItem()

    val likeItem = mainViewModel.likeItem.value

    LaunchedEffect(Unit) {
        mainViewModel.insertResult.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar("일기가 등록되었습니다.")
            } else {
                snackbarHostState.showSnackbar("등록에 실패하였습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.deleteResult.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar("일기가 삭제되었습니다.")
            } else {
                snackbarHostState.showSnackbar("삭제에 실패하였습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.getItem.collectLatest {
            if (!it) {
                snackbarHostState.showSnackbar("일기를 가져오는데 실패하였습니다.")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                        color = Color.Gray
                    )
                }
            ) {
                pages.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = item) }
                    )
                }
            }
            HorizontalPager(
                count = pages.size,
                state = pagerState
            ) { page ->
                when(page) {
                    0 -> DiaryListScreen(allItem, Modifier.fillMaxSize(), navController)
                    1 -> DiaryListScreen(likeItem, Modifier.fillMaxSize(), navController)
                }
            }
        }
    }
}