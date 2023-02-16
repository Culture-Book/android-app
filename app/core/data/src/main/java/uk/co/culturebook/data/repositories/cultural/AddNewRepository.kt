package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.*
import uk.co.culturebook.data.remote.interfaces.ApiInterface

class AddNewRepository(private val context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)

    suspend fun getCultures(location: Location) = apiInterface.getNearbyCultures(location)
    suspend fun addCulture(culture: CultureRequest) = apiInterface.addNewCulture(culture)
    suspend fun getDuplicateElements(name: String, type: ElementType) =
        apiInterface.getDuplicateElement(name, type.name)

    suspend fun postElement(element: Element, files: List<MediaFile>) =
        apiInterface.postElement(
            element = MultipartBody.Part.createFormData("element", Json.encodeToString(element)),
            files = files.mapNotNull { file ->
                file.toRequestBody(context)?.let { inputStream ->
                    MultipartBody.Part.createFormData("", "", inputStream)
                }
            }
        )

    suspend fun getDuplicateContributions(name: String, type: ElementType) =
        apiInterface.getDuplicateContribution(name, type.name)

    suspend fun postContribution(contribution: Contribution, files: List<MediaFile>) =
        apiInterface.postContribution(
            contribution = MultipartBody.Part.createFormData(
                "contribution",
                Json.encodeToString(contribution)
            ),
            files = files.mapNotNull { file ->
                file.toRequestBody(context)?.let { inputStream ->
                    MultipartBody.Part.createFormData("", "", inputStream)
                }
            }
        )
}