package uk.co.culturebook.states

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.sharedPreferences

@Composable
fun rememberAppState(): AppState {
    val context = LocalContext.current
    val isMaterialYou = context.isMaterialYou()

    return remember {
        EventBus.toggleMaterialYou(isMaterialYou)
        AppState(isMaterialYou)
    }
}

fun Context.isMaterialYou() = sharedPreferences.getBoolean(
    PrefKey.MaterialYou.key,
    false
) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S

@Stable
class AppState(isMaterialYou: Boolean = false) {
    var workerState by mutableStateOf<WorkerState>(WorkerState.Idle)
    var materialYou by mutableStateOf(isMaterialYou)
}