package com.example.diarybycompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diarybycompose.compose.AddItemDescription
import com.example.diarybycompose.compose.DetailDescription
import com.example.diarybycompose.compose.MainDescription
import com.example.diarybycompose.compose.UpdateDescription
import com.example.diarybycompose.compose.theme.DiaryByComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val navController = rememberNavController()

            viewModel.getAllItem()

            // 추후 수정
            val insertItemResult = viewModel.insertResult.value

            val allItem = viewModel.allItem.value

            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    DiaryByComposeTheme {
                        MainDescription(navController, allItem)
                    }
                }
                composable(route = "add") {
                    DiaryByComposeTheme {
                        AddItemDescription(navController) { title, content ->
                            viewModel.insertItem(title, content)
                            navController.popBackStack()
                        }
                    }
                }
                composable(route = "detail") {
                    DiaryByComposeTheme {
                        DetailDescription(navController, id, viewModel) {
                            // 인자값 넘기는 거 가능한지
                            // 바텀 내비게이션 가능한지
                            navController.navigate("update")
                        }
                    }
                }
                composable(route = "update") {
                    DiaryByComposeTheme {
                        UpdateDescription(navController)
                    }
                }
            }
        }
    }
}