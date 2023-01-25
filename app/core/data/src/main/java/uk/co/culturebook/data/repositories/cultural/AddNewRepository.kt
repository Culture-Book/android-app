package uk.co.culturebook.data.repositories.cultural

import android.content.Context
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.CultureRequest
import uk.co.culturebook.data.models.cultural.Location
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.sharedPreferences

class AddNewRepository(context: Context) {
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)
    private val sharedPrefs = context.sharedPreferences

    suspend fun getCultures(location: Location) = apiInterface.getNearbyCultures(location)
    suspend fun addCulture(culture: CultureRequest) = apiInterface.addNewCulture(culture)
}