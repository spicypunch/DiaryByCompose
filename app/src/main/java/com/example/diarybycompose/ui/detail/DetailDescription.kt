package com.example.diarybycompose.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.diarybycompose.R
import com.example.diarybycompose.data.ItemEntity

@Composable
fun DetailDescription() {
    val item = ItemEntity(title = "Test", content = "Test")
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Image")
            Text(text = item.title, modifier = Modifier.padding(8.dp), textAlign = TextAlign.Center)
            Text(text = item.content)
        }
    }
}