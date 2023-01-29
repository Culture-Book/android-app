package uk.co.culturebook.data

import android.content.Context
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.remote.retrofit.getAuthenticatedRetrofitClient
import uk.co.culturebook.data.remote.retrofit.getAuthenticationRetrofitClient

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

    fun resetInterfaces() {
        authInterface = null
        apiInterface = null
    }
}