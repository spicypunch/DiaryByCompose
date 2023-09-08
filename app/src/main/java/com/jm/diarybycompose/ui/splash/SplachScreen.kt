package com.jm.diarybycompose.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jm.diarybycompose.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    // 배경색과 로고, 그리고 앱 이름을 포함하는 중앙 정렬된 Box 컴포저블
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.round_menu_book_24),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )

            Text(text = "My Diary App", fontSize = 24.sp)
        }
    }

    // 대략적인 로딩 시간 후 다음 화면으로 이동하기 위한 Side Effect
    LaunchedEffect(key1 = true) {
        delay(2000L)
//        navController.navigate(BottomNavItem.Home.route) {
//            popUpTo("splash") { inclusive = true }
//        }
    }
}
