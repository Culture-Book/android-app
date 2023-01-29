package uk.co.culturebook.data.remote.interfaces

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.cultural.*

interface ApiInterface {
    @GET("/auth/v1/user")
    suspend fun getUser(): ApiResponse<User>

    @POST("/auth/v1/tos")
    suspend fun updateTos(): ApiResponse<Void>

    @POST("/add_new/v1/cultures")
    suspend fun getNearbyCultures(@Body location: Location): ApiResponse<CultureResponse>

    @POST("/add_new/v1/culture")
    suspend fun addNewCulture(@Body cultureRequest: CultureRequest): ApiResponse<Culture>

    @GET("/add_new/v1/element/duplicate")
    suspend fun getDuplicateElement(@Query("name") name: String, @Query("type") type: String): ApiResponse<List<Element>>
}