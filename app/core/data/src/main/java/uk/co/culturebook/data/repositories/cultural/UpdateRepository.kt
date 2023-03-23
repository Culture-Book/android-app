package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.BlockedElement
import uk.co.culturebook.data.models.cultural.FavouriteElement
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import java.util.*

class UpdateRepository(context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)

    suspend fun blockElement(uuid: UUID?) = uuid?.let {
        apiInterface.blockElement(BlockedElement(it))
    }

    suspend fun blockContribution(uuid: UUID?) = uuid?.let {
        apiInterface.blockContribution(BlockedElement(it))
    }

    suspend fun blockCulture(uuid: UUID?) = uuid?.let {
        apiInterface.blockCulture(BlockedElement(it))
    }

    suspend fun unblockElement(uuid: UUID) =apiInterface.unblockElement(uuid)

    suspend fun unblockContribution(uuid: UUID) = apiInterface.unblockContribution(uuid)

    suspend fun unblockCulture(uuid: UUID) = apiInterface.unblockCulture(uuid)

    suspend fun getBlockedList() = apiInterface.getBlockedList()

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