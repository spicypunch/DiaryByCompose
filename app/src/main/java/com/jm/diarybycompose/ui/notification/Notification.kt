package com.jm.diarybycompose.ui.notification

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    callNavController: () -> Unit,
) {
    Toast.makeText(LocalContext.current, "추가예정입니다.", Toast.LENGTH_SHORT).show()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "알림", modifier = Modifier.padding(start = 8.dp)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "noti",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { callNavController() })
                }
            )
        },
    ) {

    }
}