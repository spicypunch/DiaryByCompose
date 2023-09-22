package com.jm.diarybycompose.ui.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jm.diarybycompose.data.domain.model.NotificationStateEntity
import com.jm.diarybycompose.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingScreen(
    viewModel: MainViewModel,
    callNavController: () -> Unit,
) {
    viewModel.getNotificationState()
    val notificationState = viewModel.notificationState.value?.state
    Log.e("test", notificationState.toString())
    var switchState by remember { mutableStateOf(notificationState) }
    Log.e("test2", switchState.toString())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "설정", modifier = Modifier.padding(start = 8.dp)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { callNavController() }
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "알림 받기", fontSize = 20.sp)
                Switch(
                    checked = false,
                    onCheckedChange = { newState ->
                        switchState = false
                        viewModel.updateNotificationState(NotificationStateEntity(id = 1, state = newState))
                    }
                )
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}