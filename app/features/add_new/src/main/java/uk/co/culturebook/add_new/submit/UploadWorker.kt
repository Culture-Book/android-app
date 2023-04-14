package uk.co.culturebook.add_new.submit

import android.content.Context
import androidx.work.*
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remove
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.features.add_new.R
import uk.co.culturebook.nav.fromJsonString

const val UploadWorkerTag = "UploadWorkerTag"


fun uploadElementWorkRequest(): OneTimeWorkRequest =
    OneTimeWorkRequestBuilder<UploadWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        )
        .addTag(UploadWorkerTag)
        .build()

fun uploadContributionWorkRequest(): OneTimeWorkRequest =
    OneTimeWorkRequestBuilder<UploadWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        )
        .addTag(UploadWorkerTag)
        .build()

class UploadWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    private val addNewRepository = AddNewRepository(context)
    override suspend fun doWork(): Result {
        EventBus.registerRunningWorker(id)

        val element = applicationContext.sharedPreferences.getString(PrefKey.Element.key, null)
            ?.fromJsonString<Element>()
        val contribution =
            applicationContext.sharedPreferences.getString(PrefKey.Contribution.key, null)
                ?.fromJsonString<Contribution>()
        val files = applicationContext.sharedPreferences.getString(PrefKey.Files.key, null)
            ?.fromJsonString<List<MediaFile>>()
            ?: return Result.failure(workDataOf("error" to R.string.worker_error))

        if (element == null && contribution == null) return Result.failure(
            workDataOf("error" to R.string.worker_error)
        )

        val response = if (element != null) {
            addNewRepository.postElement(element, files)
        } else {
            contribution?.let { addNewRepository.postContribution(contribution, files) }
        }

        val result = when (response) {
            is ApiResponse.Success.Empty -> Result.success()
            is ApiResponse.Exception -> Result.failure()
            is ApiResponse.Failure -> Result.failure()
            is ApiResponse.Success -> Result.success()
            else -> Result.failure()
        }

        applicationContext.sharedPreferences.remove(PrefKey.Element)
        applicationContext.sharedPreferences.remove(PrefKey.Contribution)
        applicationContext.sharedPreferences.remove(PrefKey.Files)

        return result
    }
}