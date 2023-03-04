package uk.co.culturebook.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import uk.co.culturebook.data.flows.EventBus

@JvmInline
value class AuthenticatorInterceptor(private val token: () -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val originalRequest = chain.request()
            val requestWithAuthHeader = originalRequest.newBuilder()
                .header("Authorization", "Bearer ${token()}")
                .build()

            val response = chain.proceed(requestWithAuthHeader)
            if (response.code == 401 || response.code == 403) {
                EventBus.logout()
            }
            response
        } catch (e: Exception) {
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .code(999)
                .message("A request or response threw an exception")
                .body("$e".toResponseBody(null)).build()
        }
    }
}