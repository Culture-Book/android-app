package uk.co.culturebook.composables.app_state_handlers

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import kotlinx.coroutines.delay
import uk.co.culturebook.states.AppState
import uk.co.culturebook.states.WorkerState
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppTheme
import uk.co.culturebook.ui.theme.SystemBarsColors
import uk.co.culturebook.ui.theme.mediumFooterRoundedShape
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.surfaceColorAtElevation
import java.util.UUID


@Composable
fun HandleWorkerState(workId: UUID?, appState: AppState) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val workManager = remember { WorkManager.getInstance(context) }
    val uploadWorkInfos = workId?.let {
        workManager.getWorkInfosLiveData(WorkQuery.fromIds(it))
    }

    LaunchedEffect(uploadWorkInfos) {
        uploadWorkInfos?.observe(lifecycleOwner) { workInfos ->
            val enqueued = workInfos
                .any { workInfo ->
                    workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING
                }
            val failed = workInfos
                .any { workInfo -> workInfo.state == WorkInfo.State.FAILED || workInfo.state == WorkInfo.State.BLOCKED || workInfo.state == WorkInfo.State.CANCELLED }
            val succeeded = workInfos
                .all { workInfo -> workInfo.state == WorkInfo.State.SUCCEEDED }

            if (enqueued) {
                appState.workerState = WorkerState.ShowUploadMessage(R.string.upload_in_progress)
            } else if (failed) {
                appState.workerState = WorkerState.ShowUploadFailed(R.string.upload_failed)
            } else if (succeeded) {
                appState.workerState = WorkerState.ShowUploadComplete(R.string.upload_success)
            }
        }
    }
}

private fun AppState.getStringIdFromWorkerState() =
    (workerState as? WorkerState.ShowUploadComplete)?.stringId
        ?: (workerState as? WorkerState.ShowUploadFailed)?.stringId
        ?: (workerState as? WorkerState.ShowUploadMessage)?.stringId

@Composable
fun ShowBannerMessage(
    modifier: Modifier = Modifier,
    appState: AppState
) {
    val messageId = appState.getStringIdFromWorkerState()

    val backgroundColor = when (appState.workerState) {
        is WorkerState.ShowUploadComplete -> MaterialTheme.colorScheme.primaryContainer
        is WorkerState.ShowUploadFailed -> MaterialTheme.colorScheme.errorContainer
        is WorkerState.ShowUploadMessage -> surfaceColorAtElevation(
            MaterialTheme.colorScheme.surfaceVariant,
            smallSize
        )

        is WorkerState.Idle -> MaterialTheme.colorScheme.background
    }
    val onBackgroundColor = when (appState.workerState) {
        is WorkerState.ShowUploadComplete -> MaterialTheme.colorScheme.onPrimaryContainer
        is WorkerState.ShowUploadFailed -> MaterialTheme.colorScheme.onErrorContainer
        is WorkerState.ShowUploadMessage -> surfaceColorAtElevation(
            MaterialTheme.colorScheme.onSurfaceVariant,
            smallSize
        )

        is WorkerState.Idle -> MaterialTheme.colorScheme.onBackground
    }


    LaunchedEffect(appState.workerState) {
        when (appState.workerState) {
            is WorkerState.ShowUploadComplete -> {
                delay(5000)
                appState.workerState = WorkerState.Idle
            }

            is WorkerState.ShowUploadFailed -> {
                delay(5000)
                appState.workerState = WorkerState.Idle
            }

            else -> {}
        }
    }

    SystemBarsColors(statusBarColor = backgroundColor)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .clip(mediumFooterRoundedShape)
            .animateContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (messageId != null) {
            Text(
                modifier = Modifier.padding(mediumSize),
                text = stringResource(id = messageId),
                color = onBackgroundColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewBanner() {
    AppTheme {
        ShowBannerMessage(
            appState = AppState().apply {
                workerState = WorkerState.ShowUploadMessage(R.string.upload_in_progress)
            })
    }
}

@Preview
@Composable
fun PreviewSuccessBanner() {
    AppTheme {
        ShowBannerMessage(
            appState = AppState().apply {
                workerState = WorkerState.ShowUploadComplete(R.string.upload_success)
            })
    }
}

@Preview
@Composable
fun PreviewFailedBanner() {
    AppTheme {
        ShowBannerMessage(
            appState = AppState().apply {
                workerState = WorkerState.ShowUploadMessage(R.string.upload_failed)
            })
    }
}