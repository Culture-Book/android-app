package uk.co.culturebook.auth.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.auth.states.ForgotState
import uk.co.culturebook.auth.viewModels.ForgotPasswordViewModel
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.ShowSnackbar

@Composable
fun ForgotRoute(navController: NavController, userId: String, token: String) {
    val viewModel = viewModel {
        val userRepository = UserRepository(this[APPLICATION_KEY] as Application)
        ForgotPasswordViewModel(userRepository)
    }

    val newPassword by remember { mutableStateOf(userId.isNotEmpty() && token.isNotEmpty()) }
    val state by viewModel.forgotState.collectAsState()

    if (newPassword) {
        ForgotComposable(
            state,
            navController = navController,
            requestForgotPassword = { viewModel.passwordReset(userId, it, token) })
    } else {
        RequestForgotComposable(state, navController, viewModel::requestPasswordReset)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestForgotComposable(
    state: ForgotState,
    navController: NavController,
    requestForgotPassword: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    val snackbarState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarState) },
        topBar = { ForgotAppBar { navController.navigateTop(Route.Login) } }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(mediumSize)
                .fillMaxSize()
        ) {
            if (state is ForgotState.Error) {
                ShowSnackbar(stringId = state.messageId, snackbarState = snackbarState)
            }
            if (state is ForgotState.Success) {
                ShowSnackbar(
                    stringId = R.string.new_password_request_success,
                    snackbarState = snackbarState
                )
            }

            when (state) {
                is ForgotState.Error, ForgotState.Idle, ForgotState.Success -> {
                    Text(
                        stringResource(R.string.change_password),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier.padding(vertical = mediumSize),
                        text = stringResource(R.string.change_password_message)
                    )

                    EmailField(value = email, onValueChanged = { email = it })

                    SubmitButtonForgot(email.isValidEmail()) { requestForgotPassword(email) }
                }
                ForgotState.Loading -> LoadingComposable(padding)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotComposable(
    state: ForgotState,
    navController: NavController,
    requestForgotPassword: (String) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    @Composable
    fun ShowForm(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(mediumSize)
                .fillMaxSize()
        ) {
            Text(
                stringResource(R.string.change_password),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(vertical = mediumSize),
                text = stringResource(R.string.new_password_message)
            )

            PasswordField(
                value = password,
                confirmPassword = confirmPassword,
                onValueChanged = { password = it })
            ConfirmPasswordField(
                value = confirmPassword,
                password = password,
                onValueChanged = { confirmPassword = it })

            SubmitButtonForgot(password == confirmPassword) {
                if (password == confirmPassword) requestForgotPassword(password)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ForgotAppBar { navController.navigateTop(Route.Login) } },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }) { padding ->

        if (state is ForgotState.Error) {
            ShowSnackbar(stringId = state.messageId, snackbarState = snackbarState)
        }

        if (state is ForgotState.Success) {
            ShowSnackbar(
                stringId = R.string.new_password_success,
                onShow = { navController.navigateTop(Route.Login) },
                snackbarState = snackbarState
            )
        }

        when (state) {
            is ForgotState.Error, ForgotState.Idle, ForgotState.Success -> ShowForm(padding)
            ForgotState.Loading -> LoadingComposable(padding)
        }
    }
}


@Composable
fun SubmitButtonForgot(enabled: Boolean = false, onRegistration: () -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallSize),
        enabled = enabled,
        onClick = { onRegistration() }) {
        Text(text = stringResource(R.string.submit))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotAppBar(navigateBack: () -> Unit = {}) {
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.forgot_password)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}