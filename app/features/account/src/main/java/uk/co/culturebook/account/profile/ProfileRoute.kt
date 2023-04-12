package uk.co.culturebook.account.profile

import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.co.culturebook.account.SimpleBackAppBar
import uk.co.culturebook.data.models.authentication.enums.VerificationStatus
import uk.co.culturebook.data.models.cultural.MediaFile
import uk.co.culturebook.data.repositories.authentication.UserRepository
import uk.co.culturebook.data.utils.getFileName
import uk.co.culturebook.data.utils.getFileSize
import uk.co.culturebook.data.utils.getMimeType
import uk.co.culturebook.ui.R
import uk.co.culturebook.ui.theme.AppIcon
import uk.co.culturebook.ui.theme.mediumSize
import uk.co.culturebook.ui.theme.molecules.DisplayNameField
import uk.co.culturebook.ui.theme.molecules.EmailField
import uk.co.culturebook.ui.theme.molecules.LoadingComposable
import uk.co.culturebook.ui.theme.smallSize
import uk.co.culturebook.ui.utils.ShowSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRoute(navController: NavController) {
    val context = LocalContext.current
    val viewModel = viewModel {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
        ProfileViewModel(UserRepository(app))
    }
    val selectImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val fileName = context.getFileName(uri) ?: "file"
                val contentType = context.getMimeType(uri) ?: ""
                val fileSize = context.getFileSize(uri)
                viewModel.postEvent(
                    ProfileEvent.AddProfilePicture(
                        MediaFile(fileName, uri, fileSize, contentType)
                    )
                )
            }
        }
    val state by viewModel.state.collectAsState()
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is ProfileState.Idle) {
            viewModel.postEvent(ProfileEvent.FetchProfile)
        }
    }

    when (state) {
        is ProfileState.Error -> {
            ShowSnackbar(
                stringId = (state as ProfileState.Error).messageId,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        is ProfileState.VerificationRequested -> {
            ShowSnackbar(
                stringId = R.string.verification_requested,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        is ProfileState.PasswordUpdated -> {
            ShowSnackbar(
                stringId = R.string.password_updated,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        is ProfileState.ProfilePictureAdded -> {
            ShowSnackbar(
                stringId = R.string.profile_picture_added,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        is ProfileState.ProfilePictureRemoved -> {
            ShowSnackbar(
                stringId = R.string.profile_picture_removed,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        is ProfileState.UserUpdated -> {
            ShowSnackbar(
                stringId = R.string.user_updated,
                snackbarState = snackbarState,
                onShow = { viewModel.postEvent(ProfileEvent.FetchProfile) }
            )
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            SimpleBackAppBar(
                title = stringResource(id = R.string.profile),
                onBackTapped = { navController.navigateUp() })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarState) }
    ) { padding ->
        when (state) {
            is ProfileState.Loading -> LoadingComposable(padding)
            else -> ProfileContent(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = mediumSize),
                uiState = viewModel.uiState,
                onUpdateProfilePicture = {
                    selectImageLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onRemoveProfilePicture = { viewModel.postEvent(ProfileEvent.RemoveProfilePicture) },
                onUpdatePassword = { old, new ->
                    viewModel.postEvent(ProfileEvent.UpdatePassword(old, new))
                },
                onVerify = { reason ->
                    viewModel.postEvent(ProfileEvent.RequestVerificationStatus(reason))
                },
                submitUser = { viewModel.postEvent(ProfileEvent.UpdateUser(viewModel.uiState.toUser())) }
            )
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUIState,
    onUpdateProfilePicture: () -> Unit,
    onRemoveProfilePicture: () -> Unit,
    onUpdatePassword: (String, String) -> Unit,
    onVerify: (String) -> Unit,
    submitUser: () -> Unit
) {
    var showPasswordSheet by remember { mutableStateOf(false) }
    var showVerifySheet by remember { mutableStateOf(false) }

    if (showPasswordSheet) {
        PasswordChangeSheet(
            onDismiss = { showPasswordSheet = false },
            onPasswordChange = onUpdatePassword
        )
    }

    if (showVerifySheet) {
        VerificationRequestSheet(
            onDismiss = { showVerifySheet = false },
            onVerificationRequested = onVerify
        )
    }

    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = mediumSize)) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallSize),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(onClick = onUpdateProfilePicture) {
                    Icon(
                        painter = AppIcon.Add.getPainter(),
                        contentDescription = stringResource(id = R.string.upload_picture)
                    )
                }

                ProfilePicture()

                FilledTonalIconButton(onClick = onRemoveProfilePicture) {
                    Icon(
                        painter = AppIcon.Bin.getPainter(),
                        contentDescription = stringResource(id = R.string.remove_picture)
                    )
                }
            }
        }
        item {
            DisplayNameField(
                value = uiState.displayName,
                onValueChanged = {
                    uiState.displayName = it
                })

            EmailField(
                value = uiState.email,
                onValueChanged = {
                    uiState.email = it
                })
        }
        item {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showPasswordSheet = true }
            ) {
                Text(stringResource(id = R.string.password_change))
            }
        }
        item {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showVerifySheet = true },
                enabled = uiState.verificationStatus == VerificationStatus.NotVerified
            ) {
                val text = when (uiState.verificationStatus) {
                    VerificationStatus.NotVerified -> stringResource(id = R.string.request_verification)
                    VerificationStatus.Verified -> stringResource(id = R.string.verified_user)
                    VerificationStatus.Pending -> stringResource(id = R.string.verification_requested)
                }
                Text(text)
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = mediumSize),
                onClick = submitUser
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}