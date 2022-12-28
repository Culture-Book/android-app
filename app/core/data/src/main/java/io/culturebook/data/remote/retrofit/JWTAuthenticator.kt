package io.culturebook.data.remote.retrofit

import android.content.Context
import io.culturebook.data.SharedPrefs
import io.culturebook.data.sharedPreferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class JWTAuthenticator(private val context: Context) : Authenticator {
    @OptIn(InternalCoroutinesApi::class)
    override fun authenticate(route: Route?, response: Response): Request {
        val credential =
            context.sharedPreferences.getString(SharedPrefs.UserSession.key, "") ?: ""
        synchronized(credential) {
            return response.request.newBuilder().header("Authorization", "Bearer $credential")
                .build()
        }
    }

}