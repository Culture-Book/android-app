package uk.co.culturebook.data.remote.interfaces

import retrofit2.http.GET
import retrofit2.http.POST
import uk.co.culturebook.data.models.GenericResponse
import uk.co.culturebook.data.models.authentication.User

interface ApiInterface {
    @GET("/auth/v1/user")
    suspend fun getUser(): ApiResponse<User>

    @POST("/auth/v1/tos")
    suspend fun updateTos(): ApiResponse<GenericResponse>
}