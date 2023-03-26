package uk.co.culturebook.data.remote.interfaces

import okhttp3.MultipartBody
import retrofit2.http.*
import uk.co.culturebook.data.models.authentication.PasswordUpdateRequest
import uk.co.culturebook.data.models.authentication.User
import uk.co.culturebook.data.models.authentication.VerificationStatusRequest
import uk.co.culturebook.data.models.cultural.*
import java.util.*

interface ApiInterface {

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

    @POST("/auth/v1/user/password")
    suspend fun updatePassword(
        @Body passwordUpdate: PasswordUpdateRequest
    ): ApiResponse<Void>

    @GET("/auth/v1/user")
    suspend fun getUser(): ApiResponse<User>

    @PUT("/auth/v1/user")
    suspend fun updateUser(@Body user: User): ApiResponse<User>

    @DELETE("/auth/v1/user")
    suspend fun deleteUser(): ApiResponse<Void>

    @DELETE("/auth/v1/user/profile_picture")
    suspend fun deleteProfilePicture(): ApiResponse<Void>

    @POST("/auth/v1/user/profile_picture")
    @Streaming
    @Multipart
    suspend fun updateProfilePicture(@Part profilePicture: MultipartBody.Part): ApiResponse<Void>

    @POST("/auth/v1/user/verification_status")
    suspend fun requestVerificationStatus(@Body request: VerificationStatusRequest): ApiResponse<User>

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

    @POST("elements/v1/elements/user")
    suspend fun getUserElements(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Element>>

    @POST("elements/v1/cultures")
    suspend fun getCultures(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Culture>>

    @POST("elements/v1/cultures/user")
    suspend fun getUserCultures(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Culture>>

    @GET("elements/v1/elements/media")
    suspend fun getElementsMedia(@Query("element_id") elementId: UUID): ApiResponse<List<Media>>

    @POST("elements/v1/contributions")
    suspend fun getContributions(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Contribution>>

    @DELETE("elements/v1/contributions")
    suspend fun deleteContribution(
        @Query("contribution_id") id: UUID
    ): ApiResponse<Void>

    @DELETE("elements/v1/cultures")
    suspend fun deleteCulture(
        @Query("culture_id") id: UUID
    ): ApiResponse<Void>

    @DELETE("elements/v1/element")
    suspend fun deleteElement(
        @Query("element_id") id: UUID
    ): ApiResponse<Void>

    @POST("elements/v1/contributions/user")
    suspend fun getUserContributions(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Contribution>>

    @POST("elements/v1/contributions/favourite")
    suspend fun getFavouriteContributions(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Contribution>>

    @POST("elements/v1/cultures/favourite")
    suspend fun getFavouriteCultures(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Culture>>

    @POST("elements/v1/elements/favourite")
    suspend fun getFavouriteElements(
        @Body searchCriteria: SearchCriteria
    ): ApiResponse<List<Element>>

    @GET("elements/v1/contributions/media")
    suspend fun getContributionsMedia(@Query("contribution_id") contributionId: UUID): ApiResponse<List<Media>>

    @GET("elements/v1/element")
    suspend fun getElement(@Query("element_id") elementId: UUID): ApiResponse<Element>

    @GET("elements/v1/contribution")
    suspend fun getContribution(@Query("contribution_id") contributionId: UUID): ApiResponse<Contribution>

    @POST("elements/v1/block/element")
    suspend fun blockElement(@Body blockedElement: BlockedElement): ApiResponse<Void>

    @POST("elements/v1/block/contribution")
    suspend fun blockContribution(@Body blockedElement: BlockedElement): ApiResponse<Void>

    @POST("elements/v1/block/culture")
    suspend fun blockCulture(@Body blockedElement: BlockedElement): ApiResponse<Void>

    @DELETE("elements/v1/block/element")
    suspend fun unblockElement(@Query("element_id") id: UUID): ApiResponse<Void>

    @DELETE("elements/v1/block/contribution")
    suspend fun unblockContribution(@Query("contribution_id") id: UUID): ApiResponse<Void>

    @DELETE("elements/v1/block/culture")
    suspend fun unblockCulture(@Query("culture_id") id: UUID): ApiResponse<Void>

    @GET("elements/v1/block")
    suspend fun getBlockedList(): ApiResponse<BlockedList>

    @POST("elements/v1/favourite/element")
    suspend fun favouriteElement(@Body favouriteElement: FavouriteElement): ApiResponse<Void>

    @POST("elements/v1/favourite/contribution")
    suspend fun favouriteContribution(@Body favouriteElement: FavouriteElement): ApiResponse<Void>

    @POST("elements/v1/favourite/culture")
    suspend fun favouriteCulture(@Body favouriteElement: FavouriteElement): ApiResponse<Void>

    @GET("v1/elements/comments")
    suspend fun getElementComments(@Query("elementId") elementId: UUID): ApiResponse<List<Comment>>

    @POST("v1/elements/reactions")
    suspend fun toggleElementReaction(@Body reaction: RequestReaction): ApiResponse<Boolean>

    @POST("v1/elements/comments")
    suspend fun addElementComment(@Body comment: RequestComment): ApiResponse<Comment>

    @POST("v1/elements/comments/block")
    suspend fun blockElementComment(@Body comment: RequestComment): ApiResponse<Void>

    @DELETE("v1/elements/comments")
    suspend fun deleteElementComment(
        @Query("isContribution") isContribution: Boolean = false,
        @Query("commentId") commentId: UUID
    ): ApiResponse<Void>

    @GET("v1/contributions/comments")
    suspend fun getContributionComments(@Query("contributionId") contributionId: UUID): ApiResponse<List<Comment>>

    @POST("v1/contributions/reactions")
    suspend fun toggleContributionReaction(@Body reaction: RequestReaction): ApiResponse<Boolean>

    @POST("v1/contributions/comments")
    suspend fun addContributionComment(@Body comment: RequestComment): ApiResponse<Comment>

    @POST("v1/contributions/comments/block")
    suspend fun blockContributionComment(@Body comment: RequestComment): ApiResponse<Void>

    @DELETE("v1/contributions/comments")
    suspend fun deleteContributionComment(
        @Query("isContribution") isContribution: Boolean = true,
        @Query("commentId") commentId: UUID
    ): ApiResponse<Void>


}