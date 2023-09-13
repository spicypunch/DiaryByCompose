package com.jm.diarybycompose.ui.calendar

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jm.diarybycompose.data.domain.model.ItemEntity
import com.jm.diarybycompose.ui.MainViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    allItems: List<ItemEntity>,
    viewModel: MainViewModel,
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
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
        )
        LaunchedEffect(datePickerState.selectedDateMillis) {
            if (!viewModel.datePicker.value) {
                datePickerState.selectedDateMillis ?: return@LaunchedEffect
                onClicked(datePickerState.selectedDateMillis!!)
                viewModel.clickDatePicker(true)
            } else {
                viewModel.clickDatePicker(false)
            }
        }


        DatePicker(state = datePickerState, modifier = Modifier.fillMaxWidth())
    }

    /**
     * SDK34 요구
     */
    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("Asia/Seoul"))
//                        .toLocalDate().dayOfWeek
//                    dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY
                    val dayOfMonth =
                        Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("Asia/Seoul"))
                            .toLocalDate().monthValue
                    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
                    val laterMonthsList = getLaterMonths(currentMonth)
                    laterMonthsList.all { dayOfMonth != it }
                } else {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
                    calendar.timeInMillis = utcTimeMillis
                    calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY &&
                            calendar[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY
                }
            }

            // Allow selecting dates from year 2023 forward.
            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return year < currentYear + 1
            }
        }
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DatePicker(state = datePickerState)
    }
}

private fun getLaterMonths(currentMonth: Int): List<Int> {
    return (currentMonth + 1..12).toList()
}