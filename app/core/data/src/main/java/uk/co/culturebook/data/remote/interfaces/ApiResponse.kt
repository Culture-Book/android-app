package uk.co.culturebook.data.remote.interfaces

import uk.co.culturebook.data.logD

sealed interface ApiResponse<T : Any> {
    data class Success<T : Any>(val data: T) : ApiResponse<T> {
        class Empty<T : Any> : ApiResponse<T>
    }

    data class Failure<T : Any>(val code: Int, val message: String?) : ApiResponse<T> {
        init {
            "Failure code: $code Failure message: $message".logD()
        }
    }

    data class Exception<T : Any>(val throwable: Throwable) : ApiResponse<T> {
        init {
            throwable.message.logD()
        }
    }
}

fun <T: Any> ApiResponse<T>.getDataOrNull() = when (this) {
    is ApiResponse.Success -> data
    else -> null
}