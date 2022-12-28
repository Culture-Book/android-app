package io.culturebook.data.repositories.authentication

import io.culturebook.data.encoders.encrypt
import io.culturebook.data.models.authentication.PublicJWT
import io.culturebook.data.models.authentication.User
import io.culturebook.data.models.authentication.UserSession
import io.culturebook.data.remote.interfaces.ApiInterface
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.remote.interfaces.AuthInterface

class UserRepository(
    private val authInterface: AuthInterface,
    private val apiInterface: ApiInterface
) {
    private suspend fun getPublicOauthKey(): ApiResponse<PublicJWT> =
        authInterface.getPublicOauthKey()

    suspend fun register(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> {
                val encryptedUser = user.encodeUser(keyResponse.data.jwt)
                authInterface.register(encryptedUser)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
        }

    suspend fun login(user: User): ApiResponse<UserSession> =
        when (val keyResponse = getPublicOauthKey()) {
            is ApiResponse.Success -> {
                val encryptedUser = user.encodeUser(keyResponse.data.jwt)
                authInterface.login(encryptedUser)
            }
            is ApiResponse.Failure -> ApiResponse.Failure(keyResponse.code, keyResponse.message)
            is ApiResponse.Exception -> ApiResponse.Exception(keyResponse.throwable)
        }

    private fun User.encodeUser(publicKey: String) =
        copy(email = email.encrypt(publicKey), password = password.encrypt(publicKey))

    suspend fun getUser(): ApiResponse<User> = apiInterface.getUser()
}