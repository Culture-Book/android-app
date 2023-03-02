package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable
import uk.co.culturebook.data.serializers.URISerializer
import uk.co.culturebook.data_access.serializers.UUIDSerializer
import java.net.URI
import java.util.*

@Serializable
data class Media(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = URISerializer::class)
    val uri: URI,
    val contentType: String
)

fun Media.isVideo() = contentType.contains("video/.*".toRegex())
fun Media.isImage() = contentType.contains("image/.*".toRegex())
fun Media.isAudio() = contentType.contains("audio/.*".toRegex())