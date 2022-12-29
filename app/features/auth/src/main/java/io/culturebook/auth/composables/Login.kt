package io.culturebook.auth.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.culturebook.auth.events.LoginEvent
import io.culturebook.auth.states.LoginState
import io.culturebook.auth.viewModels.LoginViewModel
import io.culturebook.data.repositories.authentication.UserRepository
import io.culturebook.nav.navigateTop
import io.culturebook.ui.R
import io.culturebook.ui.theme.*
import io.culturebook.ui.theme.AppIcons.getPainter
import io.culturebook.ui.theme.molecules.LogoComposable

@Composable
fun LoginRoute(navController: NavController) {
    val viewModel = viewModel {
        val userRepository = UserRepository((this[APPLICATION_KEY] as Application))
        LoginViewModel(userRepository = userRepository)
    }

    val loginState by viewModel.loginState.collectAsState()
    LoginComposable(
        Modifier.padding(mediumPadding),
        navController,
        loginState,
        viewModel::postEvent
    )
}

@Composable
fun LoginComposable(
    modifier: Modifier,
    navController: NavController,
    state: LoginState,
    postEvent: (LoginEvent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is LoginState.Error, LoginState.Idle ->
                LoginComponents(
                    onLoginPressed = { email, password ->
                        postEvent(LoginEvent.Login(email, password))
                    },
                    onRegistrationPressed = { postEvent(LoginEvent.Register) }
                )
            LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Redirection -> LaunchedEffect(state) {
                navController.navigateTop(state.destination)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LoginComponents(
    onLoginPressed: (String, String) -> Unit = { _, _ -> },
    onRegistrationPressed: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordFocus by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(passwordFocus) {
        if (passwordFocus) {
            focusRequester.requestFocus()
        } else {
            focusRequester.freeFocus()
        }
    }

    LogoComposable(
        modifier = Modifier
            .padding(mediumPadding)
            .size(xxxlSize)
    )

    Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = email,
        onValueChange = { email = it },
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        keyboardOptions = emailKeyboardOptions,
        keyboardActions = KeyboardActions(onNext = { passwordFocus = true })
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding)
            .focusRequester(focusRequester),
        value = password,
        onValueChange = { password = it },
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = passwordKeyboardOptions,
        keyboardActions = KeyboardActions(onDone = { onLoginPressed(email, password) }),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = if (showPassword) AppIcons.visibility.getPainter() else AppIcons.visibility_off.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        }
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        onClick = { onLoginPressed(email, password) }) {
        Text(text = stringResource(R.string.login))
    }

    FilledTonalButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        onClick = { onRegistrationPressed() }) {
        Text(text = stringResource(R.string.register))
    }
}

