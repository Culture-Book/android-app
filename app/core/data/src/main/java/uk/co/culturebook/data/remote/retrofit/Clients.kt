package uk.co.culturebook.data.remote.retrofit

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import uk.co.culturebook.data.BuildConfig
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import java.net.CookieManager
import java.util.concurrent.TimeUnit

private val contentType = "application/json".toMediaType()
private val cookieHandler = CookieManager()
private val json = Json { ignoreUnknownKeys = true }

private val nonAuthClient by lazy {
    OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor())
        .cookieJar(JavaNetCookieJar(cookieHandler))
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
}

private fun authenticatedClient(context: Context) =
    nonAuthClient
        .newBuilder()
        .addInterceptor(AuthenticatorInterceptor)
        .authenticator(JWTAuthenticator(context))
        .build()

val imageLoaderClient by lazy {
    nonAuthClient.newBuilder().addNetworkInterceptor(MediaInterceptor()).build()
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
        .client(nonAuthClient)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .addCallAdapterFactory(ResponseCallFactory.create())
        .build()
        .create(AuthInterface::class.java)