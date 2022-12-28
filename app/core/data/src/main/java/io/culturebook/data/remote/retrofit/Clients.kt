package io.culturebook.data.remote.retrofit

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.culturebook.data.BuildConfig
import io.culturebook.data.remote.interfaces.ApiInterface
import io.culturebook.data.remote.interfaces.AuthInterface
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.CookieHandler
import java.net.CookieManager

private val contentType = "application/json".toMediaType()
private val cookieHandler = CookieManager()
private val json = Json { ignoreUnknownKeys = true }

private fun authenticatedClient(context: Context) =
    OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor())
        .authenticator(JWTAuthenticator(context))
        .cookieJar(JavaNetCookieJar(cookieHandler))
        .build()

private val authenticationClient by lazy {
    OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor())
        .cookieJar(JavaNetCookieJar(cookieHandler))
        .build()
}

@OptIn(ExperimentalSerializationApi::class)
fun getAuthenticatedRetrofitClient(context: Context): ApiInterface =
    Retrofit.Builder()
        .client(authenticatedClient(context))
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(ResponseCallFactory.create())
        .build()
        .create(ApiInterface::class.java)

@OptIn(ExperimentalSerializationApi::class)
fun getAuthenticationRetrofitClient(): AuthInterface =
    Retrofit.Builder()
        .client(authenticationClient)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .addCallAdapterFactory(ResponseCallFactory.create())
        .build()
        .create(AuthInterface::class.java)