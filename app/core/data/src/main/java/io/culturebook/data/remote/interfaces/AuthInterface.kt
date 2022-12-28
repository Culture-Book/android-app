package io.culturebook.data.remote.interfaces

import io.culturebook.data.models.authentication.PublicJWT
import io.culturebook.data.models.authentication.User
import io.culturebook.data.models.authentication.UserSession
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthInterface {
    @GET("/auth/v1/.well-known/oauth/public")
    suspend fun getPublicOauthKey(): ApiResponse<PublicJWT>

    @POST("/auth/v1/register")
    suspend fun register(@Body user: User): ApiResponse<UserSession>

    @POST("/auth/v1/login")
    suspend fun login(@Body user: User): ApiResponse<UserSession>
}