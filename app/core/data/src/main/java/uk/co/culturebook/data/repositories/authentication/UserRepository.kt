package uk.co.culturebook.data.repositories.authentication

import android.content.Context
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.encoders.encrypt
import uk.co.culturebook.data.models.authentication.PublicJWT
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.UserSession
import uk.co.culturebook.data.put
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.sharedPreferences

class UserRepository(context: Context) {
    private val authInterface: AuthInterface = Singletons.getAuthInterface()
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)
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

    suspend fun registerOrLogin(user: User?): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.registerOrLogin(user?.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
        }

    suspend fun getUser(): ApiResponse<User> = apiInterface.getUser()

    suspend fun updateTos(): ApiResponse<Void> = apiInterface.updateTos()

    fun saveUserToken(userSession: UserSession) {
        sharedPrefs.put(PrefKey.AccessToken, userSession.jwt)
        sharedPrefs.put(PrefKey.RefreshToken, userSession.refreshJwt)
    }

    fun isUserLoggedIn() = sharedPrefs.getString(PrefKey.AccessToken.key, null) != null

    private fun User.encodeUser(publicKey: String) =
        copy(email = email.encrypt(publicKey), password = password.encrypt(publicKey))

}