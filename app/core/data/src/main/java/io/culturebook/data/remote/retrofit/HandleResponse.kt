package io.culturebook.data.remote.retrofit

import io.culturebook.data.remote.interfaces.ApiResponse
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleResponse(
    execute: suspend () -> Response<T>
): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResponse.Success(body)
        } else {
            ApiResponse.Failure(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        ApiResponse.Failure(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiResponse.Exception(e)
    }
}