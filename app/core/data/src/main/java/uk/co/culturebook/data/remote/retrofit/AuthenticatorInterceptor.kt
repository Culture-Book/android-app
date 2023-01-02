package uk.co.culturebook.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import uk.co.culturebook.data.flows.EventBus

object AuthenticatorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401 || response.code == 403) {
            EventBus.logout()
        }
        return response
    }
}