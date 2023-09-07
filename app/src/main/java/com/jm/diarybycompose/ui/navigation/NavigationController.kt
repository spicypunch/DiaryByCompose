package com.jm.diarybycompose.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jm.diarybycompose.data.domain.model.BottomNavItem
import com.jm.diarybycompose.ui.MainViewModel
import com.jm.diarybycompose.ui.add.AddScreen
import com.jm.diarybycompose.ui.add.AddSpecificDateScreen
import com.jm.diarybycompose.ui.calendar.CalendarScreen
import com.jm.diarybycompose.ui.detail.DetailScreen
import com.jm.diarybycompose.ui.home.HomeScreen
import com.jm.diarybycompose.ui.search.SearchScreen
import com.jm.diarybycompose.ui.update.UpdateScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavigationController(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val viewModel = viewModel<MainViewModel>()
    viewModel.getAllItem()
    val allItems = viewModel.allItem.value
    val currentTime: Long = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)

    Box(
        modifier = Modifier.padding(paddingValues)
    ) {
        NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
            composable(route = BottomNavItem.Home.route) {
                HomeScreen(navController, allItems, viewModel) {
                    navController.navigate("search")
                }
            }
            composable(route = BottomNavItem.Add.route) {
                AddScreen(navController) { title, content, uri ->
                    viewModel.insertItem(
                        title = title,
                        content = content,
                        imageUri = uri,
                        date = dateFormat.format(Date(currentTime))
                    )
                    navController.popBackStack()
                }
            }
            composable(route = BottomNavItem.Calendar.route) {
                CalendarScreen(allItems, viewModel) { dateMillis ->
                    navController.navigate("date/$dateMillis")
                }
            }
            composable(
                route = "detail/{id}", arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                id?.let {
                    DetailScreen(navController, id, viewModel) { itemJsonString ->
                        navController.navigate(
                            "update/${
                                URLEncoder.encode(
                                    itemJsonString,
                                    StandardCharsets.UTF_8.toString()
                                )
                            }"
                        )
                    }
                }
            }

            composable(
                route = "date/{dateMillis}",
                arguments = listOf(navArgument("dateMillis") {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                val dateMillis = backStackEntry.arguments?.getLong("dateMillis")
                dateMillis?.let {
                    AddSpecificDateScreen(navController, dateMillis) { title, content, uri ->
                        viewModel.insertItem(
                            title = title,
                            content = content,
                            imageUri = uri,
                            date = dateFormat.format(Date(dateMillis))
                        )
                        navController.popBackStack()
                    }
                }
            }

            composable(route = "update/{itemJsonString}") { backStackEntry ->
                val itemJsonString = backStackEntry.arguments?.getString("itemJsonString")
                itemJsonString?.let {
                    UpdateScreen(
                        navController,
                        URLDecoder.decode(itemJsonString, StandardCharsets.UTF_8.toString())
                    ) { itemEntity ->
                        viewModel.updateItem(itemEntity)
                        navController.popBackStack()
                    }
                }
            }

            composable(route = "search") {
                SearchScreen(navController, viewModel)
            }
        }
    }
}