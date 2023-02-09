package uk.co.culturebook.data.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.database.getStringOrNull
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.IOException
import okio.source
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream

fun Context.getFileName(uri: Uri): String? {
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val cursor = contentResolver.query(uri, null, null, null)
        cursor.use {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getStringOrNull(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }

    val result = uri.path
    val cut = result?.lastIndexOf('/') ?: -1
    if (cut != -1) {
        return result?.substring(cut + 1)
    }

    return null
}

fun Context.getMimeType(uri: Uri): String? = contentResolver.getType(uri)

fun Context.uriToByteArray(uri: Uri): ByteArray {
    return BufferedInputStream(contentResolver.openInputStream(uri)).use {
        return@use it.readBytes()
    }
}

fun Context.getFileSize(uri: Uri): Long {
    val assetFileDescriptor = try {
        contentResolver.openAssetFileDescriptor(uri, "r")
    } catch (e: FileNotFoundException) {
        null
    }
    // uses ParcelFileDescriptor#getStatSize underneath if failed
    val length = assetFileDescriptor?.use { it.length } ?: -1L
    if (length != -1L) {
        return length
    }

    // if "content://" uri scheme, try contentResolver table
    if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        return contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
            ?.use { cursor ->
                // maybe shouldn't trust ContentResolver for size: https://stackoverflow.com/questions/48302972/content-resolver-returns-wrong-size
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex == -1) {
                    return@use -1L
                }
                cursor.moveToFirst()
                return try {
                    cursor.getLong(sizeIndex)
                } catch (_: Throwable) {
                    -1L
                }
            } ?: -1L
    } else {
        return -1L
    }
}

fun create(mediaType: MediaType, inputStream: InputStream): RequestBody {
    return object : RequestBody() {
        override fun contentType(): MediaType = mediaType

        override fun contentLength(): Long = -1

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            val source = inputStream.source()
            sink.writeAll(source)
        }
    }
}