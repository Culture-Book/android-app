package uk.co.culturebook.add_new.submit

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.put
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.nav.toJsonString

@Composable
fun SubmitRoute(navController: NavController, addNewState: AddNewState, onFinished: () -> Unit) {
    val context = LocalContext.current

    SubmitScreenComposable(
        onBack = { navController.navigateUp() },
        addNewState = addNewState,
        onSubmit = {
            context.sharedPreferences.put(PrefKey.Files, it.files.toJsonString())

            val worker = if (it.isContribution) {
                context.sharedPreferences.put(
                    PrefKey.Contribution,
                    it.toContribution().toJsonString()
                )
                uploadContributionWorkRequest()
            } else {
                context.sharedPreferences.put(PrefKey.Element, it.toElement().toJsonString())
                uploadElementWorkRequest()
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
