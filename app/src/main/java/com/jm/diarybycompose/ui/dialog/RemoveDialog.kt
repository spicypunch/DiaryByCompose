package com.jm.diarybycompose.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RemoveDialog(onClicked: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = { onClicked(true) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            TextButton(onClick = { onClicked(false) }) {
                Text(text = "취소")
            }
        },
        title = { Text(text = "삭제하시겠습니까?") },
        text = { Text(text = "기록하신 일기가 삭제됩니다.") }
    )
}