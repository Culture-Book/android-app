package uk.co.culturebook.auth.composables

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.auth.events.RegisterState
import uk.co.culturebook.auth.events.RegistrationEvent
import uk.co.culturebook.auth.events.toEvent
import uk.co.culturebook.auth.states.RegistrationState
import uk.co.culturebook.auth.viewModels.RegistrationViewModel
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.nav.Route
import uk.co.culturebook.nav.navigateTop
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.*
import uk.co.culturebook.ui.theme.molecules.*
import uk.co.culturebook.ui.utils.ShowSnackbar

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
    val snackbarState = remember { SnackbarHostState() }
    val registerState = remember { RegisterState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { RegistrationAppBar { navController.navigateTop(Route.Login) } },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }) { padding ->

        if (registrationState is RegistrationState.Error) {
            ShowSnackbar(
                stringId = registrationState.messageId,
                onShow = { postEvent(RegistrationEvent.Idle) },
                snackbarState = snackbarState
            )
        }

        when (registrationState) {
            is RegistrationState.Error, RegistrationState.Idle -> {
                Form(
                    modifier = Modifier.padding(padding),
                    registerState = registerState,
                    onRegistration = { postEvent(it) },
                    onTosClicked = { navController.navigate(Route.WebView.ToS.route) },
                    onPrivacyClicked = { navController.navigate(Route.WebView.Privacy.route) })
            }
            RegistrationState.Loading -> LoadingComposable(padding)
            RegistrationState.Success -> LaunchedEffect(Unit) {
                navController.navigateTop(Route.Home)
            }
        }

    }
}

@Composable
fun Form(
    modifier: Modifier,
    registerState: RegisterState,
    onRegistration: (RegistrationEvent) -> Unit,
    onTosClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(mediumSize)
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        with(registerState) {
            EmailField(email) { email = it }
            DisplayNameField(displayName) { displayName = it }
            PasswordField(password, confirmPassword) { password = it }
            ConfirmPasswordField(confirmPassword, password) { confirmPassword = it }
            ToSSwitch(tosChecked, onChanged = { tosChecked = it }) { onTosClicked() }
            PrivacySwitch(
                privacyChecked,
                onChanged = { privacyChecked = it }) { onPrivacyClicked() }
            SubmitButton(onRegistration = { onRegistration(registerState.toEvent()) })
        }
    }
}

@Composable
fun SubmitButton(onRegistration: () -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = smallSize),
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
                    painter = AppIcon.ArrowBack.getPainter(), contentDescription = "navigate back"
                )
            }
        })
}