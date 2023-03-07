package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.BlockedElement
import uk.co.culturebook.data.models.cultural.FavouriteElement
import uk.co.culturebook.data.models.cultural.SearchCriteria
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import java.util.*

class NearbyRepository(context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)

    suspend fun getElements(searchCriteria: SearchCriteria) =
        apiInterface.getElements(searchCriteria)

    suspend fun getElementMedia(elementId: UUID?) =
        elementId?.let { apiInterface.getElementsMedia(it) }

    suspend fun getContributions(searchCriteria: SearchCriteria) =
        apiInterface.getContributions(searchCriteria)

    suspend fun getContributionMedia(contributionId: UUID?) =
        contributionId?.let { apiInterface.getContributionsMedia(it) }

    suspend fun getCultures(searchCriteria: SearchCriteria) =
        apiInterface.getCultures(searchCriteria)

    suspend fun blockElement(uuid: UUID?) = uuid?.let {
        apiInterface.blockElement(
            BlockedElement(it)
        )
    }

    suspend fun blockContribution(uuid: UUID?) = uuid?.let {
        apiInterface.blockContribution(BlockedElement(it))
    }

    suspend fun blockCulture(uuid: UUID?) = uuid?.let {
        apiInterface.blockCulture(BlockedElement(it))
    }

    suspend fun favouriteElement(uuid: UUID?) = uuid?.let {
        apiInterface.favouriteElement(
            FavouriteElement(it)
        )
    }

    suspend fun favouriteContribution(uuid: UUID?) = uuid?.let {
        apiInterface.favouriteContribution(FavouriteElement(it))
    }

    suspend fun favouriteCulture(uuid: UUID?) = uuid?.let {
        apiInterface.favouriteCulture(FavouriteElement(it))
    }

}