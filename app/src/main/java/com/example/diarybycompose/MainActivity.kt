package com.example.diarybycompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diarybycompose.compose.AddItemDescription
import com.example.diarybycompose.compose.DetailDescription
import com.example.diarybycompose.compose.MainDescription
import com.example.diarybycompose.compose.UpdateDescription
import com.example.diarybycompose.compose.theme.DiaryByComposeTheme
import com.example.diarybycompose.data.ItemEntity
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
                composable(route = "detail/{itemJsonString}") { backStackEntry ->
                    val itemJsonString = backStackEntry.arguments?.getString("itemJsonString")
                    DiaryByComposeTheme {
                        itemJsonString?.let {
                            DetailDescription(navController, itemJsonString, viewModel) { itemJsonString ->
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