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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import io.culturebook.ui.theme.molecules.TertiarySwitchSurface

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
                val errorString = stringResource(registrationState.message)
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
                            .padding(padding), contentAlignment = Center) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        keyboardOptions = defaultEmailKeyboardOptions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayNameField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.display_name)) },
        singleLine = true,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(value: String, confirmPassword: String, onValueChanged: (String) -> Unit) {
    var showPassword by remember { mutableStateOf(false) }
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        isError = value != confirmPassword,
        supportingText = {
            if (value != confirmPassword) Text(stringResource(R.string.password_match))
        },
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = defaultPasswordKeyboardOptions,
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    painter = if (showPassword) AppIcons.visibility.getPainter() else AppIcons.visibility_off.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordField(value: String, password: String, onValueChanged: (String) -> Unit) {
    var showConfirmPassword by remember { mutableStateOf(false) }
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        value = value,
        onValueChange = onValueChanged,
        shape = mediumRoundedShape,
        label = { Text(stringResource(R.string.confirm_password)) },
        singleLine = true,
        isError = password != value,
        supportingText = {
            if (password != value) Text(stringResource(R.string.password_match))
        },
        visualTransformation = if (!showConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = defaultPasswordKeyboardOptions,
        trailingIcon = {
            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                Icon(
                    painter = if (showConfirmPassword) AppIcons.visibility.getPainter() else AppIcons.visibility_off.getPainter(),
                    contentDescription = "password_visibility"
                )
            }
        })

}

@Composable
fun ToSSwitch(isChecked: Boolean, onChanged: (Boolean) -> Unit, onClick: () -> Unit) {
    TertiarySwitchSurface(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        title = stringResource(R.string.tos),
        subtitle = stringResource(R.string.read_tos),
        checked = isChecked,
        onSwitchChanged = { onChanged(it) },
        onSubtitleClicked = { onClick() })
}

@Composable
fun PrivacySwitch(isChecked: Boolean, onChanged: (Boolean) -> Unit, onClick: () -> Unit) {
    TertiarySwitchSurface(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallPadding),
        title = stringResource(R.string.privacy),
        subtitle = stringResource(R.string.read_privacy),
        checked = isChecked,
        onSwitchChanged = { onChanged(it) },
        onSubtitleClicked = { onClick() })
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