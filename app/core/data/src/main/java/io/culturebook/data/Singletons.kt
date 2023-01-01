package io.culturebook.data

import android.content.Context
import io.culturebook.data.remote.interfaces.ApiInterface
import io.culturebook.data.remote.interfaces.AuthInterface
import io.culturebook.data.remote.retrofit.getAuthenticatedRetrofitClient
import io.culturebook.data.remote.retrofit.getAuthenticationRetrofitClient

object Singletons {
    private var authInterface: AuthInterface? = null
    private var apiInterface: ApiInterface? = null

    fun getAuthInterface(): AuthInterface =
        authInterface.let {
            if (it == null) {
                getAuthenticationRetrofitClient().also { authInt ->
                    authInterface = authInt
                    return authInt
                }
            } else {
                return it
            }
        }

    fun getApiInterface(context: Context): ApiInterface =
        apiInterface.let {
            if (it == null) {
                getAuthenticatedRetrofitClient(context).also { apiInt ->
                    apiInterface = apiInt
                    return apiInt
                }
            } else {
                return it
            }
        }
}