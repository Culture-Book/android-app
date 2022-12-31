package io.culturebook.auth.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.culturebook.auth.events.RegisterState
import io.culturebook.auth.events.RegistrationEvent
import io.culturebook.auth.events.toEvent
import io.culturebook.auth.states.RegistrationState
import io.culturebook.auth.viewModels.RegistrationViewModel
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.nav.Route
import io.culturebook.nav.navigateTop
import io.culturebook.ui.R
import io.culturebook.ui.theme.*
import io.culturebook.ui.theme.AppIcons.getPainter
import io.culturebook.ui.theme.molecules.*

@Composable
fun RegistrationRoute(navController: NavController) {
    val viewModel = viewModel {
        val userRepository = UserRepository(this[APPLICATION_KEY] as Application)
        RegistrationViewModel(userRepository)
    }

    val state by viewModel.registrationState.collectAsState()
    RegistrationComposable(state, navController, viewModel::postEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationComposable(
    registrationState: RegistrationState,
    navController: NavController,
    postEvent: (RegistrationEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarState = remember { SnackbarHostState() }

    val registerState = remember { RegisterState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { RegistrationAppBar { navController.navigateTop(Route.Login) } },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(mediumPadding)
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            if (registrationState is RegistrationState.Error) {
                val errorString = stringResource(registrationState.messageId)
                LaunchedEffect(errorString) {
                    snackbarState.showSnackbar(errorString)
                }
            }

            when (registrationState) {
                is RegistrationState.Error, RegistrationState.Idle -> Form(registerState = registerState,
                    onRegistration = { postEvent(it) },
                    onTosClicked = { navController.navigate(Route.WebView.ToS.route) },
                    onPrivacyClicked = { navController.navigate(Route.WebView.Privacy.route) })
                RegistrationState.Loading ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(padding), contentAlignment = Center
                    ) {
                        CircularProgressIndicator()
                    }
                RegistrationState.Success -> navController.navigateTop(Route.Nearby)
            }
        }
    }

}

@Composable
fun Form(
    registerState: RegisterState,
    onRegistration: (RegistrationEvent) -> Unit,
    onTosClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
) {
    with(registerState) {
        EmailField(email) { email = it }
        DisplayNameField(displayName) { displayName = it }
        PasswordField(password, confirmPassword) { password = it }
        ConfirmPasswordField(confirmPassword, password) { confirmPassword = it }
        ToSSwitch(tosChecked, onChanged = { tosChecked = it }) { onTosClicked() }
        PrivacySwitch(privacyChecked, onChanged = { privacyChecked = it }) { onPrivacyClicked() }
        SubmitButton(onRegistration = { onRegistration(registerState.toEvent()) })
    }

}

@Composable
fun SubmitButton(onRegistration: () -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        onClick = { onRegistration() }) {
        Text(text = stringResource(R.string.register))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegistrationAppBar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.register)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcons.arrow_back.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}