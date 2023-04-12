package uk.co.culturebook.data.repositories.authentication

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import uk.co.culturebook.data.PrefKey
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.encoders.encrypt
import uk.co.culturebook.data.flows.EventBus
import uk.co.culturebook.data.models.authentication.*
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.models.cultural.toRequestBody
import uk.co.culturebook.data.put
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.sharedPreferences
import java.util.*

class UserRepository(private val context: Context) {
    private val authInterface: AuthInterface = Singletons.getAuthInterface()
    private val apiInterface: ApiInterface = Singletons.getApiInterface(context)
    private val sharedPrefs = context.sharedPreferences

    private suspend fun getPublicOauthKey(): ApiResponse<PublicJWT> =
        sharedPrefs.getString(PrefKey.PublicKey.key, null)?.let {
            ApiResponse.Success(PublicJWT(it))
        } ?: run {
            authInterface.getPublicOauthKey().also {
                if (it is ApiResponse.Success) {
                    sharedPrefs.put(PrefKey.PublicKey, it.data.jwt)
                }
            }
        }

    suspend fun register(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.register(user.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
        }

    suspend fun login(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.login(user.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
        }

    suspend fun registerOrLogin(user: User?): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> authInterface.registerOrLogin(user?.encodeUser(keyResponse.data.jwt))
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
        }

    suspend fun getUser(): ApiResponse<User> = apiInterface.getUser().also {
        if (it is ApiResponse.Success) {
            sharedPrefs.put(PrefKey.User, Json.encodeToString(it.data))
        }
    }

    suspend fun updateTos(): ApiResponse<Void> = apiInterface.updateTos()

    suspend fun requestPasswordReset(email: String): ApiResponse<Void> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
            is ApiResponse.Success ->
                authInterface.requestPasswordReset(
                    PasswordResetRequest(email = email.encrypt(keyResponse.data.jwt))
                )
        }

    suspend fun passwordReset(
        userId: String,
        password: String,
        token: UUID
    ): ApiResponse<Void> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
            is ApiResponse.Success ->
                authInterface.resetPassword(
                    PasswordReset(
                        userId = userId.encrypt(keyResponse.data.jwt),
                        password = password.encrypt(keyResponse.data.jwt),
                        token = token
                    )
                )
        }

    suspend fun updatePassword(oldPassword: String, newPassword: String) =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Success.Empty -> ApiResponse.Success.Empty()
            is ApiResponse.Success ->
                apiInterface.updatePassword(
                    PasswordUpdateRequest(
                        oldPassword.encrypt(keyResponse.data.jwt),
                        newPassword.encrypt(keyResponse.data.jwt)
                    )
                )
        }

    suspend fun updateUser(user: User) = apiInterface.updateUser(user)

    suspend fun deleteAccount() = apiInterface.deleteUser().also {
        if (it is ApiResponse.Success) {
            EventBus.logout()
        }
    }

    suspend fun updateProfilePicture(image: MediaFile) =
        image.toRequestBody(context).let {
            apiInterface.updateProfilePicture(MultipartBody.Part.createFormData("", "", it))
        }

    suspend fun removeProfilePicture() = apiInterface.deleteProfilePicture()

    suspend fun requestVerificationStatus(reason: String) =
        apiInterface.requestVerificationStatus(
            VerificationStatusRequest(reason)
        )

    fun saveUserToken(userSession: UserSession) {
        sharedPrefs.put(PrefKey.AccessToken, userSession.jwt)
        sharedPrefs.put(PrefKey.RefreshToken, userSession.refreshJwt)
    }

    fun isUserLoggedIn() = sharedPrefs.getString(PrefKey.AccessToken.key, null) != null

    private fun User.encodeUser(publicKey: String) =
        copy(email = email.encrypt(publicKey), password = password.encrypt(publicKey))

}