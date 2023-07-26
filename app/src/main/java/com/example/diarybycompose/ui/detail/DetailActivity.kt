package com.example.diarybycompose.ui.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.diarybycompose.ui.theme.DiaryByComposeTheme

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryByComposeTheme {
                DetailDescription()
            }
        }
    }
}