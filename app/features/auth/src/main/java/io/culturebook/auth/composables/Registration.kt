package io.culturebook.auth.composables

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import io.culturebook.auth.viewModels.RegistrationViewModel
import io.culturebook.data.repositories.authentication.UserRepository

@Composable
fun RegistrationRoute() {
    val viewModel = viewModel {
        val userRepository = UserRepository(this[APPLICATION_KEY] as Application)
        RegistrationViewModel(userRepository)
    }

    RegistrationComposable()
}

@Composable
fun RegistrationComposable() {

}