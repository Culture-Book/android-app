package uk.co.culturebook.data.remote.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import java.io.IOException
import java.io.InputStream

class InputStreamRequestBody(private val inputStream: InputStream) : RequestBody() {
    override fun contentLength(): Long = try {
        inputStream.available().toLong()
    } catch (e: IOException) {
        -1
    }

    override fun contentType() = "application/octet-stream".toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        try {
            inputStream.use {
                it.source().use { source ->
                    sink.writeAll(source.buffer())
                }
            }
        } catch (_: Exception) {

        }
    }
}