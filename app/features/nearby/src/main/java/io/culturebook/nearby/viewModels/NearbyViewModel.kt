package io.culturebook.nearby.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.culturebook.data.logD
import io.culturebook.data.logE
import io.culturebook.data.remote.interfaces.ApiResponse
import io.culturebook.data.repositories.authentication.UserRepository
import kotlinx.coroutines.launch

class NearbyViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val user = userRepository.getUser()
            when(user) {
                is ApiResponse.Success -> user.data.email.logD()
                is ApiResponse.Failure -> user.message.logD()
                is ApiResponse.Exception -> user.throwable.logE()
            }
        }
    }
}