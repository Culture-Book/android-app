package uk.co.culturebook.data.models.cultural

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Location(val latitude: Double, val longitude: Double)

val Location.Companion.Empty get() = Location(0.0, 0.0)

val LatLng.location get() = Location(latitude, longitude)

val Location.latLng get() = LatLng(latitude, longitude)

fun LatLng.isEmpty() = latitude == 0.0 && longitude == 0.0
fun LatLng.isNotEmpty() = !isEmpty()

fun Location?.isEmpty() = this?.latitude == 0.0 && longitude == 0.0
fun Location?.isNotEmpty() = !isEmpty()