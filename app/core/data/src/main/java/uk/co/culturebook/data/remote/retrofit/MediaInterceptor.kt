package uk.co.culturebook.data.remote.retrofit

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import okhttp3.Interceptor
import okhttp3.Response
import uk.co.culturebook.data.Constants
import uk.co.culturebook.data.remote_config.RemoteConfig

class MediaInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = Firebase.remoteConfig.getString(RemoteConfig.MediaToken.key).trim()
        val apiKey = Firebase.remoteConfig.getString(RemoteConfig.MediaApiKey.key).trim()
        val request = chain.request().newBuilder()
            .header(Constants.AuthorizationHeader, Constants.getBearerValue(token))
            .header(Constants.ApiKeyHeader, apiKey)
            .build()
        return chain.proceed(request)
    }
}