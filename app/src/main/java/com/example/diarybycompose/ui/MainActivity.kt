package com.example.diarybycompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.example.diarybycompose.ui.theme.DiaryByComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaryByComposeTheme {
                MainDescription(modifier = Modifier)
            }
        }
    }
}

