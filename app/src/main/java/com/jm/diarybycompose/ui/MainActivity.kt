package com.jm.diarybycompose.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.jm.diarybycompose.data.domain.model.BottomNavItem
import com.jm.diarybycompose.ui.navigation.NavigationController
import com.jm.diarybycompose.ui.theme.DiaryByComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFCMToken()
        setContent {
            DiaryByComposeTheme {
                val permissionsList: Array<String> =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.CAMERA,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } else {
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.POST_NOTIFICATIONS
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
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Add,
        BottomNavItem.Map,
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
        NavigationController(navController, it)
    }
}

private fun getFCMToken(): String? {
    var token: String? = null
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.e(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }
        token = task.result
        Log.d(TAG, "FCM Token is $token")
    })
    return token
}