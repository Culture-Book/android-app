package uk.co.culturebook.add_new.submit

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop

@Composable
fun SubmitRoute(navController: NavController, addNewState: AddNewState, onFinished: () -> Unit) {
    val viewModel = viewModel {
        val addNewRepository =
            AddNewRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        SubmitViewModel(addNewRepository)
    }
    val state by viewModel.submitState.collectAsState()
    val context = LocalContext.current

    SubmitScreenComposable(
        onBack = { navController.navigateUp() },
        addNewState = addNewState,
        state = state,
        onSubmit = {
            val worker = if (it.isContribution) {
                uploadContributionWorkRequest(it)
            } else {
                uploadElementWorkRequest(it)
            }

            WorkManager.getInstance(context).enqueueUniqueWork(
                UploadWorkerTag,
                ExistingWorkPolicy.REPLACE,
                worker
            )

            navController.navigateTop(Route.Home)
            onFinished()
        }
    )
}
