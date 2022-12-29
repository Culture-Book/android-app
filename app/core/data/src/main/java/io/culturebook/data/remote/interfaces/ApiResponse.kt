package io.culturebook.data.remote.interfaces

import io.culturebook.data.logD

sealed interface ApiResponse<T : Any> {
    data class Success<T : Any>(val data: T) : ApiResponse<T>
    data class Failure<T : Any>(val code: Int, val message: String?) : ApiResponse<T> {
        init {
            message.logD()
        }
    }

    data class Exception<T : Any>(val throwable: Throwable) : ApiResponse<T> {
        init {
            throwable.message.logD()
        }
    }
}