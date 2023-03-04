package uk.co.culturebook.data.remote.retrofit

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import uk.co.culturebook.data.*
import uk.co.culturebook.data.models.authentication.UserSession
import uk.co.culturebook.data.remote.interfaces.ApiResponse

class RefreshJWT(private val context: Context) : Authenticator {
    private val authInterface = Singletons.getAuthInterface()
    private var accessToken: String? = null

    private val Response.reachedMaxAttempts: Boolean
        get() = this.priorResponse?.priorResponse?.priorResponse != null

    private suspend fun attemptRefresh(accessToken: String, refreshToken: String): String? {
        var credential: String? = null

        when (val refreshed = authInterface.refreshJwt(UserSession(accessToken, refreshToken))) {
            is ApiResponse.Exception -> refreshed.throwable.logE()
            is ApiResponse.Failure -> refreshed.message.logE()
            is ApiResponse.Success.Empty -> {}
            is ApiResponse.Success -> {
                with(context.sharedPreferences) {
                    put(PrefKey.AccessToken, refreshed.data.jwt)
                    put(PrefKey.RefreshToken, refreshed.data.refreshJwt)
                }
                credential = refreshed.data.jwt
            }
        }
        return credential
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.reachedMaxAttempts) return null

        if (accessToken == null) {
            accessToken = context.sharedPreferences.getString(PrefKey.AccessToken.key, "") ?: ""
        }

        if (response.code != 401) {
            return null
        }

        if (response.request.header("Authorization") != null) {
            runBlocking {
                val refreshToken =
                    context.sharedPreferences.getString(PrefKey.RefreshToken.key, "") ?: ""
                accessToken = attemptRefresh(accessToken!!, refreshToken) ?: accessToken
            }
        }

        return response.request.newBuilder().header("Authorization", "Bearer $accessToken")
            .build()
    }
}