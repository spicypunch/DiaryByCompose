package com.jm.diarybycompose.compose

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    onClicked: (String, String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val (title, setTitle) = rememberSaveable {
        mutableStateOf("")
    }
    val (content, setContent) = rememberSaveable {
        mutableStateOf("")
    }

    var imageUri: Uri? = null

    val intent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

    val imageFromCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {

            } else {

            }
        }

    val imageFromStorage =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri ->
                    imageUri = uri
                } ?: run {

                }
            } else if (it.resultCode != Activity.RESULT_CANCELED) {

            }
        }

//    val takePhotoFromAlbumIntent =
//        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
//            type = "image/*"
//            action = Intent.ACTION_GET_CONTENT
//            putExtra(
//                Intent.EXTRA_MIME_TYPES,
//                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
//            )
//            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "일기 쓰기") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier.clickable { navController.popBackStack() })
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = setTitle,
                    label = { Text(text = "제목") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp)
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = setContent,
                    label = { Text(text = "내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
                Button(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        imageFromStorage.launch(intent)
                    }) {
                    Text(text = "사 진\n추 가")
                }
                Log.e("test", imageUri.toString())
                Image(painter = rememberImagePainter(data = imageUri), contentDescription = null)
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        onClicked(title, content)
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("빈칸을 채워주세요.") }
                    }
                }
            ) {
                Text(text = "등록")
            }
        }
    }
}

private fun imageIntent() {
    val intent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
}