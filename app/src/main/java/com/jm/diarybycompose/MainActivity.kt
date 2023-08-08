package com.jm.diarybycompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jm.diarybycompose.compose.AddScreen
import com.jm.diarybycompose.compose.DetailScreen
import com.jm.diarybycompose.compose.MainScreen
import com.jm.diarybycompose.compose.SettingScreen
import com.jm.diarybycompose.compose.UpdateScreen
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
            DiaryByComposeTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    val currentTime: Long = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("M월 d일 h시 m분", Locale.KOREA)
    val viewModel = viewModel<MainViewModel>()

    viewModel.getAllItem()

    val allItem = viewModel.allItem.value
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Add,
        BottomNavItem.Setting,
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                navItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        alwaysShowLabel = false,
                        label = { Text(screen.title, fontSize = 9.sp) },
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = screen.title
                            )
                        })
                }
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
                composable(route = BottomNavItem.Home.route) {
                    MainScreen(navController, allItem, viewModel)
                }
                composable(route = BottomNavItem.Add.route) {
                    AddScreen(navController) { title, content ->
                        viewModel.insertItem(
                            title,
                            content,
                            dateFormat.format(Date(currentTime))
                        )
                        navController.popBackStack()
                    }
                }

                composable(route = BottomNavItem.Setting.route) {
                    SettingScreen()
                }

                composable(
                    route = "detail/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.IntType
                        })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id")
                    id?.let {
                        DetailScreen(navController, id, viewModel) { itemJsonString ->
                            navController.navigate("update/${itemJsonString}")
                        }
                    }
                }

                composable(route = "update/{itemJsonString}") { backStackEntry ->
                    val itemJsonString = backStackEntry.arguments?.getString("itemJsonString")
                    itemJsonString?.let {
                        UpdateScreen(navController, itemJsonString) { itemEntity ->
                            viewModel.updateItem(itemEntity)
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}