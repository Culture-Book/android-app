package uk.co.culturebook.data.remote.retrofit

import uk.co.culturebook.data.remote.interfaces.HttpClient

class ClientFactory {
    companion object {
        private lateinit var apiInstance: HttpClient
        private lateinit var authInstance: HttpClient
        fun getApiInstance(instance: HttpClient): HttpClient {
            if (!this::apiInstance.isInitialized) {
                apiInstance = instance
            }
            return apiInstance
        }

        fun getAuthInstance(instance: HttpClient): HttpClient {
            if (!this::authInstance.isInitialized) {
                authInstance = instance
            }
            return authInstance
        }
    }
}