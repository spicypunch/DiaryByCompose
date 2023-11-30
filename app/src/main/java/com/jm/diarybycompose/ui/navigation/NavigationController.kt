package com.jm.diarybycompose.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jm.diarybycompose.data.domain.model.BottomNavItem
import com.jm.diarybycompose.data.domain.model.MenuItem
import com.jm.diarybycompose.ui.MainViewModel
import com.jm.diarybycompose.ui.add.AddScreen
import com.jm.diarybycompose.ui.detail.DetailScreen
import com.jm.diarybycompose.ui.home.HomeScreen
import com.jm.diarybycompose.ui.map.MapScreen
import com.jm.diarybycompose.ui.notification.Notification
import com.jm.diarybycompose.ui.search.SearchScreen
import com.jm.diarybycompose.ui.setting.SettingScreen
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
    paddingValues: PaddingValues,
    viewModel: MainViewModel = viewModel()
) {
    viewModel.getAllItem()
    val allItems = viewModel.allItem.value
    val currentTime: Long = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
    Box(
        modifier = Modifier.padding(paddingValues)
    ) {
        NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
//            composable(route = "splash") {
//                SplashScreen(navController)
//            }
            composable(route = BottomNavItem.Home.route) {
                HomeScreen(
                    allItems,
                    viewModel,
                    callNavController = { id ->
                        navController.navigate("detail/$id")
                    },
                    onClicked = {
                        navController.navigate("search")
                    },
                    onClickedMenu = { route ->
                        navController.navigate("$route")
                    }
                )
            }
            composable(route = BottomNavItem.Add.route) {
                AddScreen(
                    callNavController = {
                        navController.popBackStack()
                    },
                    onClicked = { title, content, uri, latLng->
                        viewModel.insertItem(
                            title = title,
                            content = content,
                            imageUri = uri,
                            date = dateFormat.format(Date(currentTime)),
                            latLng = latLng
                        )
                        navController.popBackStack()
                    }
                )
            }
//            composable(route = BottomNavItem.Calendar.route) {
//                CalendarScreen(allItems, viewModel) { dateMillis ->
//                    navController.navigate("date/$dateMillis")
//                }
//            }
            composable(route = BottomNavItem.Map.route) {
                MapScreen()
            }
            composable(
                route = "detail/{id}", arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                id?.let {
                    DetailScreen(
                        id,
                        viewModel,
                        callNavController = {
                            navController.popBackStack()
                        },
                        onClicked = { itemJsonString ->
                            navController.navigate(
                                "update/${
                                    URLEncoder.encode(
                                        itemJsonString,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                }"
                            )
                        }
                    )
                }
            }

//            composable(
//                route = "date/{dateMillis}",
//                arguments = listOf(navArgument("dateMillis") {
//                    type = NavType.LongType
//                })
//            ) { backStackEntry ->
//                val dateMillis = backStackEntry.arguments?.getLong("dateMillis")
//                dateMillis?.let {
//                    AddSpecificDateScreen(
//                        dateMillis,
//                        callNavController = {
//                            navController.popBackStack()
//                        },
//                        onClicked = { title, content, uri ->
//                            viewModel.insertItem(
//                                title = title,
//                                content = content,
//                                imageUri = uri,
//                                date = dateFormat.format(Date(dateMillis))
//                            )
//                            navController.popBackStack()
//                        }
//                    )
//                }
//            }

            composable(route = "update/{itemJsonString}") { backStackEntry ->
                val itemJsonString = backStackEntry.arguments?.getString("itemJsonString")
                itemJsonString?.let {
                    UpdateScreen(
                        URLDecoder.decode(itemJsonString, StandardCharsets.UTF_8.toString()),
                        callNavController = {
                            navController.popBackStack()
                        },
                        onClicked = { itemEntity ->
                            viewModel.updateItem(itemEntity)
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(route = "search") {
                SearchScreen(viewModel) {
                    navController.navigate("detail/$id")
                }
            }

            composable(route = MenuItem.Notification.route) {
                Notification(
                    callNavController = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = MenuItem.Setting.route) {
                SettingScreen(
                    callNavController = {
                        navController.popBackStack()
                    },
                )
            }

//            composable(route = "setting_notification") {
//                SettingNotificationScreen(
//                    callNavController = {
//                        navController.popBackStack()
//                    }
//                )
//            }
        }
    }
}