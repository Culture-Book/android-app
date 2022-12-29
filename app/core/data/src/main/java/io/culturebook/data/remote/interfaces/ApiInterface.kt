package io.culturebook.data.remote.interfaces

import io.culturebook.data.models.authentication.User
import retrofit2.http.GET

interface ApiInterface {
    @GET("/auth/v1/user")
    suspend fun getUser(): ApiResponse<User>
}