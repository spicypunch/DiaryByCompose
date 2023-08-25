package com.jm.diarybycompose.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    Kalendar(currentDay = LocalDate(), kalendarType = KalendarType.Oceanic) {

    }
}