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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    DiaryByComposeTheme {
                        MainDescription(navController)
                    }
                }
                composable(route = "add") {
                    DiaryByComposeTheme {
                        AddItemDescription(navController) {

                        }
                    }
                }
                composable(route = "detail") {
                    DiaryByComposeTheme {
                        DetailDescription(navController) {
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