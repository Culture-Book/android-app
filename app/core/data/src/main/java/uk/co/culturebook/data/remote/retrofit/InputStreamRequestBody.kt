package uk.co.culturebook.data.remote.retrofit

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import uk.co.culturebook.data.logD
import uk.co.culturebook.data.logE
import uk.co.culturebook.data.models.cultural.MediaFile

class InputStreamRequestBody(
    private val context: Context,
    private val mediaFile: MediaFile,
) : RequestBody() {
    override fun contentLength(): Long = mediaFile.fileSize

    override fun contentType() = mediaFile.contentType.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        try {
            context.contentResolver.openInputStream(mediaFile.uri)?.use {
                val source = it.source()
                var totalTransferred = 0L
                var read = 0L

                while (read != -1L) {
                    read = source.read(sink.buffer, DEFAULT_BUFFER_SIZE.toLong())
                    totalTransferred += read
                    sink.flush()
                }

                "Total uploaded: $totalTransferred".logD(this::class.java.simpleName)
            }
        } catch (e: Exception) {
            e.logE(this::class.java.simpleName)
        }
    }
}