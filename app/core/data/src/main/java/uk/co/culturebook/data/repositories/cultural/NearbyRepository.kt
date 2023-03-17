package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.SearchCriteria
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import java.util.*

class NearbyRepository(context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)

    suspend fun getElements(searchCriteria: SearchCriteria) =
        apiInterface.getElements(searchCriteria)

    suspend fun getElement(elementId: UUID?) =
        elementId?.let { apiInterface.getElement(it) }

    suspend fun getElementMedia(elementId: UUID?) =
        elementId?.let { apiInterface.getElementsMedia(it) }

    suspend fun getContributions(searchCriteria: SearchCriteria) =
        apiInterface.getContributions(searchCriteria)

    suspend fun getContribution(contributionId: UUID?) =
        contributionId?.let { apiInterface.getContribution(it) }

    suspend fun getContributionMedia(contributionId: UUID?) =
        contributionId?.let { apiInterface.getContributionsMedia(it) }

    suspend fun getCultures(searchCriteria: SearchCriteria) =
        apiInterface.getCultures(searchCriteria)

}