package uk.co.culturebook.data.models.cultural

import kotlinx.serialization.Serializable

/**
 * A generic media file to be stored.
 *
 * @param fileName File's name, including the extension.
 * @param bucketName The bucket in which the file is saved to.
 * @param data The data of the File in a [ByteArray] form
 * */
@Serializable
data class MediaFile(
    val fileName: String,
    val bucketName: String,
    val data: ByteArray,
    val contentType: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        if (other !is MediaFile) return false

        if (fileName != other.fileName) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}

const val BucketNameKey = "bucketName"