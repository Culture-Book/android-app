package uk.co.culturebook.ui.theme.molecules

import android.text.format.DateFormat
import android.view.ContextThemeWrapper
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppTheme
import uk.co.culturebook.ui.theme.surfaceColorAtElevation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Composable
fun DateTimeDialog(
    minDate: LocalDate = LocalDate.now(),
    pickedDate: LocalDateTime? = LocalDateTime.now(),
    onDateChanged: (LocalDateTime) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var showTimer by remember { mutableStateOf(false) }
    var date by remember {
        val date =
            if (pickedDate?.isBefore(LocalDateTime.now()) == true) LocalDateTime.now() else pickedDate
                ?: LocalDateTime.now()
        mutableStateOf(date)
    }
    val backgroundColor = surfaceColorAtElevation(
        MaterialTheme.colorScheme.surface,
        elevation = AlertDialogDefaults.TonalElevation
    )
    val timePickerTheme =
        if (isSystemInDarkTheme()) R.style.Theme_TimePicker_Dark else R.style.Theme_TimePicker_Light
    val datePickerTheme =
        if (isSystemInDarkTheme()) R.style.Theme_DatePicker_Dark else R.style.Theme_DatePicker_Light


    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            if (showTimer) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        val timePicker =
                            TimePicker(ContextThemeWrapper(context, timePickerTheme))
                        timePicker.setIs24HourView(DateFormat.is24HourFormat(context))

                        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                            if (timePicker.validateInput()) {
                                date = LocalDateTime.of(
                                    date.year,
                                    date.month,
                                    date.dayOfMonth,
                                    hourOfDay,
                                    minute
                                )
                            }
                        }
                        timePicker.also {
                            date = LocalDateTime.of(
                                date.year,
                                date.month,
                                date.dayOfMonth,
                                it.hour,
                                it.minute
                            )
                        }
                    })
            } else {
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = { context ->
                        val datePicker =
                            DatePicker(ContextThemeWrapper(context, datePickerTheme))
                        datePicker.minDate =
                            Date.from(minDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
                        datePicker.updateDate(
                            date.year,
                            date.month.value - 1,
                            date.dayOfMonth
                        )
                        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
                            date = LocalDateTime.of(
                                year,
                                month + 1,
                                dayOfMonth,
                                date.hour,
                                date.minute
                            )
                        }
                        datePicker.setBackgroundColor(backgroundColor.toArgb())
                        datePicker.also {
                            date = LocalDateTime.of(
                                it.year,
                                it.month,
                                it.dayOfMonth,
                                date.hour,
                                date.minute
                            )
                        }
                    })
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            if (!showTimer) {
                TextButton(onClick = { showTimer = true }) {
                    Text(stringResource(R.string.next))
                }
            } else {
                TextButton(onClick = {
                    onDateChanged(date)
                }) {
                    Text(stringResource(R.string.confirm))
                }
            }
        })

}

@Composable
@Preview
private fun PreviewDateDialog() {
    AppTheme {
        DateTimeDialog()
    }
}