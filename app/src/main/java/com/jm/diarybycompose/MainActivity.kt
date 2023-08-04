package com.jm.diarybycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jm.diarybycompose.compose.AddItemDescription
import com.jm.diarybycompose.compose.DetailDescription
import com.jm.diarybycompose.compose.MainDescription
import com.jm.diarybycompose.compose.UpdateDescription
import com.jm.diarybycompose.compose.theme.DiaryByComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTime: Long = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("M월 d일 h시 m분", Locale.KOREA)
            val viewModel = viewModel<MainViewModel>()
            val navController = rememberNavController()

            viewModel.getAllItem()

            val allItem = viewModel.allItem.value

            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    DiaryByComposeTheme {
                        MainDescription(navController, allItem, viewModel)
                    }
                }
                composable(route = "add") {
                    DiaryByComposeTheme {
                        AddItemDescription(navController) { title, content ->
                            viewModel.insertItem(title, content, dateFormat.format(Date(currentTime)))
                            navController.popBackStack()
                        }
                    }
                }
                composable(route = "detail/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toInt()
                    DiaryByComposeTheme {
                        id?.let {
                            DetailDescription(navController, id, viewModel) { itemJsonString ->
                                navController.navigate("update/${itemJsonString}")
                            }
                        }
                    }
                }

                composable(route = "update/{itemJsonString}") { backStackEntry ->
                    val itemJsonString = backStackEntry.arguments?.getString("itemJsonString")
                    DiaryByComposeTheme {
                        itemJsonString?.let {
                            UpdateDescription(navController, itemJsonString) { itemEntity ->
                                viewModel.updateItem(itemEntity)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}