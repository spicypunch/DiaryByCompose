package com.jm.diarybycompose.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jm.diarybycompose.data.domain.model.BottomNavItem
import com.jm.diarybycompose.ui.add.AddScreen
import com.jm.diarybycompose.ui.add.AddSpecificDateScreen
import com.jm.diarybycompose.ui.calendar.CalendarScreen
import com.jm.diarybycompose.ui.detail.DetailScreen
import com.jm.diarybycompose.ui.home.HomeScreen
import com.jm.diarybycompose.ui.theme.DiaryByComposeTheme
import com.jm.diarybycompose.ui.update.UpdateScreen
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryByComposeTheme {
                val permissionsList: Array<String> =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.CAMERA
                        )
                    } else {
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    }
                var grantedList by remember { mutableStateOf(mutableListOf(false)) }

                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
                        grantedList = isGranted.values.toMutableList()
                    }

                grantedList = permissionsList.map {
                    ContextCompat.checkSelfPermission(
                        LocalContext.current,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }.toMutableList()

                if (grantedList.count { it } == grantedList.size) {
                    App()
                } else {
                    DemandPermissionScreen() {
                        launcher.launch(permissionsList)
                    }
                }
            }
        }
    }
}

@Composable
fun DemandPermissionScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "권한을 허용해주세요.")
        Button(onClick = { onClick() }) {
            Text(text = "권한 요청")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    val currentTime: Long = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
    val viewModel = viewModel<MainViewModel>()

    viewModel.getAllItem()

    val allItems = viewModel.allItem.value

    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Add,
        BottomNavItem.Calendar,
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
                        }
                    )
                }
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
                composable(route = BottomNavItem.Home.route) {
                    HomeScreen(navController, allItems, viewModel)
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
                    CalendarScreen(allItems) { dateMillis ->
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
            }
        }
    }
}