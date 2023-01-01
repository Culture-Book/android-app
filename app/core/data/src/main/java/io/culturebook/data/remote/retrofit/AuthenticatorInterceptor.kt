package io.culturebook.data.remote.retrofit

import io.culturebook.data.flows.EventBus
import okhttp3.Interceptor
import okhttp3.Response

object AuthenticatorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(response.code == 401 || response.code == 403) {
            EventBus.logout()
        }
        return response
    }
}