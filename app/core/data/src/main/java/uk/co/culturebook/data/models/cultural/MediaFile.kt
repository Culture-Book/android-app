package uk.co.culturebook.data.models.cultural

import android.content.Context
import android.net.Uri
import uk.co.culturebook.data.remote.retrofit.InputStreamRequestBody
import uk.co.culturebook.data.serializers.AndroidURISerializer

@kotlinx.serialization.Serializable
data class MediaFile(
    val fileName: String,
    @kotlinx.serialization.Serializable(with = AndroidURISerializer::class)
    val uri: Uri,
    val fileSize: Long,
    val contentType: String
)

fun MediaFile.toRequestBody(context: Context) = context.contentResolver.openInputStream(uri)
    ?.use { InputStreamRequestBody(it) }

fun MediaFile.isContentTypeValid() =
    contentType.contains("video/.*".toRegex()) ||
            contentType.contains("image/.*".toRegex()) ||
            contentType.contains("audio/.*".toRegex())

fun String.isContentTypeValid() =
    contains("video/.*".toRegex()) || contains("image/.*".toRegex()) || contains("audio/.*".toRegex())

fun List<MediaFile>.smallerThan50Mb() = totalSize() <= 50_000_000
private fun List<MediaFile>.totalSize() = fold(0L) { acc, file -> acc + file.fileSize }