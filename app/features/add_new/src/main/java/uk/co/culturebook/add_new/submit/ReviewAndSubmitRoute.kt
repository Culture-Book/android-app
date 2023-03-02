package uk.co.culturebook.add_new.submit

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop

@Composable
fun SubmitRoute(navController: NavController, addNewState: AddNewState, onFinished: () -> Unit) {
    val context = LocalContext.current

    SubmitScreenComposable(
        onBack = { navController.navigateUp() },
        addNewState = addNewState,
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
