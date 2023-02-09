package uk.co.culturebook.data.models.cultural

import android.net.Uri
import uk.co.culturebook.data.remote.retrofit.InputStreamRequestBody
import java.io.InputStream

data class MediaFile(
    val fileName: String,
    val uri: Uri,
    val inputStream: InputStream,
    val fileSize: Long,
    val contentType: String
)

fun MediaFile.toRequestBody() = InputStreamRequestBody(this)

fun MediaFile.isContentTypeValid() =
    contentType.contains("video/.*".toRegex()) ||
            contentType.contains("image/.*".toRegex()) ||
            contentType.contains("audio/.*".toRegex())

fun String.isContentTypeValid() =
    contains("video/.*".toRegex()) || contains("image/.*".toRegex()) || contains("audio/.*".toRegex())

fun List<MediaFile>.smallerThan50Mb() = totalSize() <= 50_000_000
private fun List<MediaFile>.totalSize() = fold(0L) { acc, file -> acc + file.fileSize }