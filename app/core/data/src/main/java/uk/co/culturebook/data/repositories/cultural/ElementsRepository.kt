package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.modules.culture.data.models.SearchCriteria
import java.util.*

class ElementsRepository(private val context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)

    suspend fun getElements(searchCriteria: SearchCriteria) =
        apiInterface.getElements(searchCriteria)

    suspend fun getElementMedia(elementId: UUID) = apiInterface.getElementsMedia(elementId)

    suspend fun getContributions(searchCriteria: SearchCriteria) =
        apiInterface.getContributions(searchCriteria)

    suspend fun getContributionMedia(contributionId: UUID) = apiInterface.getContributionsMedia(contributionId)

}