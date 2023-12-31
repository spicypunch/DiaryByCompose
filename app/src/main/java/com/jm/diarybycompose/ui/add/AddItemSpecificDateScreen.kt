package com.jm.diarybycompose.ui.add

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSpecificDateScreen(
    dateMillis: Long,
    callNavController: () -> Unit,
    onClicked: (String, String, Uri?) -> Unit
) {
    val (title, setTitle) = rememberSaveable {
        mutableStateOf("")
    }
    val (content, setContent) = rememberSaveable {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current

    val dateFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)
    val date = dateFormat.format(Date(dateMillis))
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = date, modifier = Modifier.padding(start = 8.dp)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { callNavController() })
                }
            )
        },
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
                Row {
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            TedImagePicker.with(context).start { uri ->
                                imageUri = uri
                            }
                        }) {
                        Text(text = "사 진\n추 가")
                    }
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 16.dp)
                            .padding(start = 8.dp)
                            .aspectRatio(1f)
                            .clip(RectangleShape),
                        painter = rememberImagePainter(data = imageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    if (title.isNotEmpty() && content.isNotEmpty()) {
                        onClicked(title, content, imageUri)
                    }
                },
                enabled = title.isNotEmpty() && content.isNotEmpty()
            ) {
                Text(text = "등록")
            }
        }
    }
}