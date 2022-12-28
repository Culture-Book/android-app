package io.culturebook.data.repositories.authentication

import android.content.Context
import io.culturebook.data.PrefKey
import io.culturebook.data.encoders.encrypt
import io.culturebook.data.models.authentication.PublicJWT
import io.culturebook.data.models.authentication.User
import io.culturebook.data.models.authentication.UserSession
import io.culturebook.data.put
import io.culturebook.data.remote.interfaces.ApiInterface
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.remote.interfaces.AuthInterface
import io.culturebook.data.remote.retrofit.getAuthenticatedRetrofitClient
import io.culturebook.data.remote.retrofit.getAuthenticationRetrofitClient
import io.culturebook.data.sharedPreferences

class UserRepository(context: Context) {
    private val authInterface: AuthInterface = getAuthenticationRetrofitClient()
    private val apiInterface: ApiInterface = getAuthenticatedRetrofitClient(context)
    private val sharedPrefs = context.sharedPreferences

    private suspend fun getPublicOauthKey(): ApiResponse<PublicJWT> =
        authInterface.getPublicOauthKey()

    suspend fun register(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.register(user.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
        }

    suspend fun login(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.login(user.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
        }

    fun saveUserToken(userSession: UserSession) {
        sharedPrefs.put(PrefKey.UserSession, userSession.jwt)
    }

    private fun User.encodeUser(publicKey: String) =
        copy(email = email.encrypt(publicKey), password = password.encrypt(publicKey))

    suspend fun getUser(): ApiResponse<User> = apiInterface.getUser()
}