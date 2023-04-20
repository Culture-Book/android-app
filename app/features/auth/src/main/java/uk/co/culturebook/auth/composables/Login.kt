package uk.co.culturebook.auth.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.co.culturebook.auth.composables.molecules.GoogleSignInButton
import uk.co.culturebook.auth.events.LoginEvent
import uk.co.culturebook.auth.events.LoginHState
import uk.co.culturebook.auth.states.LoginState
import uk.co.culturebook.auth.viewModels.LoginViewModel
import uk.co.culturebook.data.logE
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.theme.xxxlSize

@Composable
fun LoginRoute(navigate: (String) -> Unit, navigateTop: (String) -> Unit) {
    val viewModel = viewModel {
        val userRepository = UserRepository((this[APPLICATION_KEY] as Application))
        LoginViewModel(userRepository = userRepository)
    }

    val loginState by viewModel.loginState.collectAsState()
    LoginComposable(
        Modifier.padding(mediumSize),
        navigate,
        navigateTop,
        loginState,
        viewModel::postEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(
    modifier: Modifier,
    navigate: (String) -> Unit,
    navigateTop: (String) -> Unit,
    state: LoginState,
    postEvent: (LoginEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val loginState = remember { LoginHState() }
    val scrollState = rememberScrollState()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state is LoginState.Error) {
                val errorMessage = stringResource(state.message)
                LaunchedEffect(state) {
                    snackbarHostState.showSnackbar(errorMessage)
                    postEvent(LoginEvent.Idle)
                }
            }
            when (state) {
                is LoginState.Error, LoginState.Idle -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Form(
                            loginState,
                            onLoginPressed = { postEvent(LoginEvent.Login(it.email, it.password)) },
                        )

                        GoogleSignInButton(modifier = Modifier.fillMaxWidth()) {
                            when {
                                it.isSuccessful -> postEvent(LoginEvent.GoogleLogin(it.result))
                                !it.isSuccessful -> {
                                    postEvent(LoginEvent.Error(R.string.google_crapped))
                                    it.exception.logE()
                                }
                            }
                        }

                        OrDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(smallSize)
                        )

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = { navigate(Route.Registration.route) }) {
                            Text(text = stringResource(R.string.register))
                        }

                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = { navigate(Route.Forgot.route) }) {
                            Text(text = stringResource(R.string.forgot_password))
                        }
                    }
                }

                LoginState.Loading -> LoadingComposable(paddingValues)
                is LoginState.Success -> LaunchedEffect(Unit) {
                    navigateTop(Route.Home.route)
                }
            }
        }
    }
}

@Composable
fun Form(
    loginHState: LoginHState = LoginHState(),
    onLoginPressed: (LoginHState) -> Unit = { _ -> }
) {
    val focusRequester = remember { FocusRequester() }
    var passwordFocus by remember { mutableStateOf(false) }

    LaunchedEffect(passwordFocus) {
        if (passwordFocus) {
            focusRequester.requestFocus()
        }
    }

    LogoComposable(
        modifier = Modifier
            .padding(mediumSize)
            .size(xxxlSize)
    )

    Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)

    LoginEmailField(
        value = loginHState.email,
        onValueChanged = { loginHState.email = it },
        onNext = { passwordFocus = it }
    )

    LoginPasswordField(
        value = loginHState.password,
        focusRequester = focusRequester,
        onValueChanged = { loginHState.password = it },
        onDone = { onLoginPressed(loginHState) }
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallSize),
        onClick = { onLoginPressed(loginHState) }) {
        Text(text = stringResource(R.string.login))
    }
}

