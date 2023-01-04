package uk.co.culturebook.data.remote.retrofit

import retrofit2.HttpException
import retrofit2.Response
import uk.co.culturebook.data.remote.interfaces.ApiResponse

suspend fun <T : Any> handleResponse(
    execute: suspend () -> Response<T>
): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            if (body != null) ApiResponse.Success(body) else ApiResponse.Success.Empty()
        } else {
            ApiResponse.Failure(
                code = response.code(),
                message = response.errorBody()?.string()?.removeSurrounding("\"", "\"")
            )
        }
    } catch (e: HttpException) {
        ApiResponse.Failure(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiResponse.Exception(e)
    }
}