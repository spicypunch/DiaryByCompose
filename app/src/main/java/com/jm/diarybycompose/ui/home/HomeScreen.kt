package com.jm.diarybycompose.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.data.domain.model.MenuItem
import com.jm.diarybycompose.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    allItems: List<ItemEntity>,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pages = listOf("일기", "❤️")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val items = listOf(MenuItem.Notification, MenuItem.Setting)

    viewModel.getLikeItem()

    val likeItem = viewModel.likeItem.value

    LaunchedEffect(Unit) {
        viewModel.insertResult.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar("일기가 등록되었습니다.")
            } else {
                snackbarHostState.showSnackbar("등록에 실패하였습니다.")
            }
        }
        viewModel.deleteResult.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar("일기가 삭제되었습니다.")
            } else {
                snackbarHostState.showSnackbar("삭제에 실패하였습니다.")
            }
        }
        viewModel.getItem.collectLatest {
            if (!it) {
                snackbarHostState.showSnackbar("일기를 가져오는데 실패하였습니다.")
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, menuItem ->
                        NavigationDrawerItem(
                            label = { Text(text = menuItem.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    painter = rememberAsyncImagePainter(model = if (index == selectedItemIndex) menuItem.selectedIcon else menuItem.unselectedIcon),
                                    contentDescription = menuItem.title
                                )
                            },
                            badge = {
                                if (menuItem.title == "Notification") Text(text = "12")
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "일기장") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        backgroundColor = Color.Transparent,
                        indicator = { tabPositions ->
                            SecondaryIndicator(
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
                        when (page) {
                            0 -> DiaryListScreen(
                                allItems,
                                viewModel,
                                Modifier.fillMaxSize(),
                                navController
                            )

                            1 -> DiaryListScreen(
                                likeItem,
                                viewModel,
                                Modifier.fillMaxSize(),
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}