package uk.co.culturebook.data.remote.interfaces

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uk.co.culturebook.data.models.authentication.*

interface AuthInterface {
    @GET("/auth/v1/.well-known/oauth/public")
    suspend fun getPublicOauthKey(): ApiResponse<PublicJWT>

    @POST("/auth/v1/register")
    suspend fun register(@Body user: User): ApiResponse<UserSession>

    @POST("/auth/v1/login")
    suspend fun login(@Body user: User): ApiResponse<UserSession>

    @POST("/auth/v1/register-login")
    suspend fun registerOrLogin(@Body user: User?): ApiResponse<UserSession>

    @POST("/auth/v1/jwt-refresh")
    suspend fun refreshJwt(@Body userSession: UserSession): ApiResponse<UserSession>

    @POST("/auth/v1/forgot")
    suspend fun requestPasswordReset(@Body passwordReset: PasswordResetRequest): ApiResponse<Void>

    @POST("/auth/v1/reset-password")
    suspend fun resetPassword(@Body passwordReset: PasswordReset): ApiResponse<Void>
}