package uk.co.culturebook.ui.theme.molecules

import android.text.format.DateFormat
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    header: @Composable ColumnScope.() -> Unit = {
        Divider(
            modifier = Modifier
                .padding(mediumSize)
                .size(width = xlSize, height = xsSize)
                .clip(mediumRoundedShape)
                .align(CenterHorizontally)
        )
    },
    footer: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .padding(mediumSize),
                onClick = { onDismiss() }
            ) {
                Text(stringResource(R.string.close))
            }

            FilledTonalButton(
                modifier = Modifier
                    .padding(mediumSize),
                onClick = { onConfirm() }
            ) {
                Text(stringResource(R.string.done))
            }
        }
    },
    content: @Composable () -> Unit,
) {
    val swipeableState = rememberSwipeableState(0.5f)
    var contentSize by remember { mutableStateOf(IntSize(0, 1)) }
    val localConfig = LocalConfiguration.current

    val expandedTarget = with(LocalDensity.current) { (-contentSize.height.dp).toPx() }
    val anchors = mapOf(
        -expandedTarget to 0f,
        0f to 0.5f
    )

    LaunchedEffect(swipeableState.targetValue, swipeableState.isAnimationRunning) {
        if (swipeableState.targetValue == 0f && !swipeableState.isAnimationRunning) {
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val dialogWindowProvider = LocalView.current.parent as? DialogWindowProvider
        dialogWindowProvider?.window?.setGravity(Gravity.BOTTOM)

        Box(
            modifier = Modifier
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical,
                    velocityThreshold = SwipeableDefaults.VelocityThreshold * 2,
                )
                .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        contentSize = it.size
                    }
                    .align(Alignment.BottomCenter),
                shape = mediumHeaderRoundedShape
            ) {
                Column(modifier = Modifier.padding(horizontal = mediumSize)) {
                    header()
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(
                                min = 0.dp,
                                max = localConfig.screenHeightDp.dp * .9f
                            )
                    ) {
                        item {
                            content()
                        }
                        item {
                            Box(modifier = Modifier.padding(vertical = mediumSize)) {
                                footer()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewDateDialog() {
    AppTheme {
        DateTimeDialog()
    }
}

@Composable
@Preview
private fun PreviewSheetDialog() {
    AppTheme {
        var show by remember { mutableStateOf(true) }
        Column {
            Text(modifier = Modifier.padding(16.dp), text = "hello")
            Text(modifier = Modifier.padding(16.dp), text = "hello")
            Text(modifier = Modifier.padding(16.dp), text = "hello")
        }

        if (show) {
            ModalBottomSheet(onDismiss = { show = false }, onConfirm = {}) {
                LazyColumn() {
                    items(buildList {
                        for (i in 1..100) {
                            add("hello $i")
                        }
                    }) {
                        Text(it)
                    }
                }
            }
        }
    }
}