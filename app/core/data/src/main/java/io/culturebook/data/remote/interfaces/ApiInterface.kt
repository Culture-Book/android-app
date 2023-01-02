package io.culturebook.data.remote.interfaces

import io.culturebook.data.models.authentication.User
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("/auth/v1/user")
    suspend fun getUser(): ApiResponse<User>

    @POST("/auth/v1/tos")
    suspend fun updateTos(): ApiResponse<Void>
}