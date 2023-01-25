package uk.co.culturebook.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlin.math.*

private val locationFlow = MutableStateFlow<LocationStatus>(LocationStatus.Fetching)
val LocationFlow = locationFlow.asStateFlow()

private val request =
    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 30000).build()

private val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        runBlocking {
            val locationStatus = locationResult.lastLocation?.let { LocationStatus.Success(it) }
                ?: LocationStatus.Error
            locationFlow.emit(locationStatus)
        }
    }
}

@SuppressLint("MissingPermission")
fun registerForLocationUpdates(context: Context) {
    LocationServices.getFusedLocationProviderClient(context.applicationContext)
        .requestLocationUpdates(request, locationCallback, null)
}

fun unregisterLocationUpdates(context: Context) {
    LocationServices.getFusedLocationProviderClient(context.applicationContext)
        .removeLocationUpdates(locationCallback)
}
