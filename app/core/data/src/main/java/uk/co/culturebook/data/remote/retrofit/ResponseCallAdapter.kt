package uk.co.culturebook.data.remote.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import uk.co.culturebook.data.remote.interfaces.ApiResponse
import java.lang.reflect.Type

class ResponseCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call)
    }
}