package com.jm.diarybycompose.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.jm.diarybycompose.data.domain.model.ItemEntity
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    allItems: List<ItemEntity>,
    onClicked: (Long) -> Unit
    ) {
    val list = ArrayList<Long>()
    allItems.forEach { item ->
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일")

        val date = LocalDate.parse(item.date, formatter)

        val dateMillis = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        list.add(dateMillis)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val state = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
//            initialSelectedDateMillis = if (list.isNotEmpty()) list[0] else null
        )
        LaunchedEffect(state.selectedDateMillis) {
            state.selectedDateMillis ?: return@LaunchedEffect
            onClicked(state.selectedDateMillis!!)
        }
        DatePicker(state = state, modifier = Modifier.fillMaxWidth())
    }
}