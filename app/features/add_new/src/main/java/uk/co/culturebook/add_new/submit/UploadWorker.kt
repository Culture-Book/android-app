package uk.co.culturebook.add_new.submit

import android.content.Context
import androidx.work.*
import uk.co.culturebook.add_new.data.ElementState
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.models.cultural.Element
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.repositories.cultural.AddNewRepository
import uk.co.culturebook.nav.fromJsonString
import uk.co.culturebook.nav.toJsonString
import uk.co.culturebook.ui.R

const val UploadWorkerTag = "UploadWorkerTag"


fun uploadWorkRequest(elementState: ElementState): OneTimeWorkRequest =
    OneTimeWorkRequestBuilder<UploadWorker>()
        .setInputData(
            workDataOf(
                "element" to elementState.toElement().toJsonString(),
                "files" to elementState.files.toJsonString()
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
            ?: return Result.failure(workDataOf("error" to R.string.generic_sorry))
        val files = inputData.getString("files")?.fromJsonString<List<MediaFile>>()
            ?: return Result.failure(workDataOf("error" to R.string.generic_sorry))

        val result =
            when (addNewRepository.postElement(element, files)) {
                is ApiResponse.Success.Empty -> Result.success()
                is ApiResponse.Exception -> Result.failure()
                is ApiResponse.Failure -> Result.failure()
                is ApiResponse.Success -> Result.success()
            }

        return result
    }
}