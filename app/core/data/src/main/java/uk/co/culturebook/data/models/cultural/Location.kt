package uk.co.culturebook.data.models.cultural

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Location(val latitude: Double, val longitude: Double)

fun LatLng.toLocation() = Location(latitude, longitude)
fun Location.toLatLng() = LatLng(latitude, longitude)
fun android.location.Location.toLatLng() = LatLng(latitude, longitude)
fun LatLng.isEmpty() = latitude == 0.0 && longitude == 0.0
fun LatLng.isNotEmpty() = !isEmpty()
