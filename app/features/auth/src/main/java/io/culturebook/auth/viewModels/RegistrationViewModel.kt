package io.culturebook.auth.viewModels

import androidx.lifecycle.ViewModel
import io.culturebook.data.repositories.authentication.UserRepository

class RegistrationViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

}