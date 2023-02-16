package uk.co.culturebook.add_new.submit

import android.content.Context
import androidx.work.*
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.models.cultural.Contribution
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.nav.fromJsonString
import uk.co.culturebook.nav.toJsonString
import uk.co.culturebook.ui.R

const val UploadWorkerTag = "UploadWorkerTag"


fun uploadElementWorkRequest(addNewState: AddNewState): OneTimeWorkRequest =
    OneTimeWorkRequestBuilder<UploadWorker>()
        .setInputData(
            workDataOf(
                "element" to addNewState.toElement().toJsonString(),
                "files" to addNewState.files.toJsonString()
            )
        )
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        )
        .addTag(UploadWorkerTag)
        .build()

fun uploadContributionWorkRequest(addNewState: AddNewState): OneTimeWorkRequest =
    OneTimeWorkRequestBuilder<UploadWorker>()
        .setInputData(
            workDataOf(
                "contribution" to addNewState.toContribution().toJsonString(),
                "files" to addNewState.files.toJsonString()
            )
        )
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

        val element = inputData.getString("element")?.fromJsonString<Element>()
        val contribution = inputData.getString("contribution")?.fromJsonString<Contribution>()
        val files = inputData.getString("files")?.fromJsonString<List<MediaFile>>()
            ?: return Result.failure(workDataOf("error" to R.string.generic_sorry))

        if (element == null && contribution == null) return Result.failure(
            workDataOf("error" to R.string.generic_sorry)
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

        return result
    }
}