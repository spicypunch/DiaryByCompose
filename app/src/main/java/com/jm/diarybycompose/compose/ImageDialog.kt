package com.jm.diarybycompose.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ImageDialog(
//    showDialog: MutableState<Boolean>,
//    onCameraOptionSelected: () -> Unit,
//    onGalleryOptionSelected: () -> Unit
) {
//    if (showDialog.value) {
    AlertDialog(
        onDismissRequest = {
//                showDialog.value = false
        },
        title = {
            Text(text = "옵션 선택")
        },
        text = {
            Text(text = "카메라 또는 갤러리에서 이미지를 가져오세요.")
        },
        confirmButton = {
            Button(
                onClick = {
//                        onCameraOptionSelected()
//                        showDialog.value = false
                }
            ) {
                Text("카메라")
            }
        },
        dismissButton = {
            Button(
                onClick = {
//                        onGalleryOptionSelected()
//                        showDialog.value = false
                }
            ) {
                Text("갤러리")
            }
        }
    )
}


@Preview
@Composable
fun Preview() {
    ImageDialog()
}
