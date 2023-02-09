package uk.co.culturebook.data.remote.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import uk.co.culturebook.data.models.cultural.MediaFile
import java.io.IOException

class InputStreamRequestBody(private val mediaFile: MediaFile) : RequestBody() {
    override fun contentLength(): Long = try {
        mediaFile.inputStream.available().toLong()
    } catch (e: IOException) {
        -1
    }

    override fun contentType() = "application/octet-stream".toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        try {
            mediaFile.inputStream.use {
                it.source().use { source ->
                    sink.writeAll(source.buffer())
                }
            }
        } catch (_: Exception) {

        }
    }
}