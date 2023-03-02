package uk.co.culturebook.data.remote.interfaces

import okhttp3.MultipartBody
import retrofit2.http.*
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.cultural.*
import java.util.*

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
    suspend fun getDuplicateElement(
        @Query("name") name: String,
        @Query("type") type: String
    ): ApiResponse<List<Element>>

    @Streaming
    @Multipart
    @POST("/add_new/v1/element/submit")
    suspend fun postElement(
        @Part element: MultipartBody.Part,
        @Part files: List<MultipartBody.Part>
    ): ApiResponse<Element>

    @GET("/add_new/v1/contribution/duplicate")
    suspend fun getDuplicateContribution(
        @Query("name") name: String,
        @Query("type") type: String
    ): ApiResponse<List<Contribution>>

    @Streaming
    @Multipart
    @POST("/add_new/v1/contribution/submit")
    suspend fun postContribution(
        @Part contribution: MultipartBody.Part,
        @Part files: List<MultipartBody.Part>
    ): ApiResponse<Contribution>

    @POST("elements/v1/elements")
    suspend fun getElements(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Element>>

    @GET("elements/v1/elements/media")
    suspend fun getElementsMedia(@Query("element_id") elementId: UUID): ApiResponse<List<Media>>

    @POST("elements/v1/contributions")
    suspend fun getContributions(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Element>>

    @GET("elements/v1/contributions/media")
    suspend fun getContributionsMedia(@Query("contribution_id") contributionId: UUID): ApiResponse<List<Media>>
}