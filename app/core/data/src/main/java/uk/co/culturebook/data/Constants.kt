package uk.co.culturebook.data

object Constants {
    const val AuthorizationHeader = "Authorization"
    const val ApiKeyHeader = "apiKey"
    fun getBearerValue(value: String) = "Bearer $value"
}