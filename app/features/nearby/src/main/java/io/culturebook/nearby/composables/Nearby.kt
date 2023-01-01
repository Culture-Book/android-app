package io.culturebook.nearby.composables

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.nearby.viewModels.NearbyViewModel

@Composable
fun NearbyRoute() {
    val viewModel = viewModel {
        val userRepository = UserRepository((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application))
        NearbyViewModel(userRepository = userRepository)
    }

    NearbyComposable()
}

@Composable
fun NearbyComposable() {
    Text(text = "NEARBY")
}