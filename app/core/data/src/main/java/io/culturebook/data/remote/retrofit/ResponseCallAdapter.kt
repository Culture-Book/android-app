package io.culturebook.data.remote.retrofit

import io.culturebook.data.remote.interfaces.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResponseCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call)
    }
}