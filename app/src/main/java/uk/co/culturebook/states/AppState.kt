package uk.co.culturebook.states

import androidx.compose.runtime.*

@Composable
fun rememberAppState() = remember { AppState() }

@Stable
class AppState {
    var workerState by mutableStateOf<WorkerState>(WorkerState.Idle)
}