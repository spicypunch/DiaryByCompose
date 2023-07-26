package com.example.diarybycompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.diarybycompose.compose.MainDescription
import com.example.diarybycompose.compose.theme.DiaryByComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryByComposeTheme {
                MainDescription()
            }
        }
    }
}

