@file:OptIn(ExperimentalPermissionsApi::class)

package ru.abramovkirill.languageapp.profile.impl.ui.screens.profile

import android.Manifest
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import ru.abramovkirill.languageapp.common.utils.compose.ScreenSideEffectsFlowHandler
import ru.abramovkirill.languageapp.common.utils.compose.handleMessageSideEffect
import ru.abramovkirill.languageapp.common.utils.createTempPictureUri
import ru.abramovkirill.languageapp.common.utils.grantReadUriPermission
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.composables.AppRootContainer
import ru.abramovkirill.languageapp.core.design.composables.AppSingleSelectBottomSheet
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonDefaults
import ru.abramovkirill.languageapp.core.design.composables.button.AppButtonState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppBarState
import ru.abramovkirill.languageapp.core.design.composables.scaffold.AppScaffoldControllerEffect
import ru.abramovkirill.languageapp.core.design.composables.scaffold.LocalAppScaffoldController
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.isAppInDarkTheme
import ru.abramovkirill.languageapp.core.design.utils.COMPOSE_LARGE_DEVICE_SPEC
import ru.abramovkirill.languageapp.core.design.utils.smallDeviceMaxWidth
import ru.abramovkirill.languageapp.profile.impl.R
import ru.abramovkirill.languageapp.profile.impl.di.ProfileComponent
import ru.abramovkirill.languageapp.profile.impl.ui.composables.ProfileAppBar
import ru.abramovkirill.languageapp.profile.impl.ui.composables.ProfileAppBarShimmer
import ru.abramovkirill.languageapp.profile.impl.ui.navigation.PROFILE_SCREEN_ROUTE_PATH
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.Intent
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.SideEffect
import ru.abramovkirill.languageapp.profile.impl.ui.screens.profile.ProfileContract.State
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
internal fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffects = viewModel.container.sideEffectFlow

    ScreenSideEffects(
        sideEffects = sideEffects,
        onIntent = viewModel::processIntent
    )

    ProfileScreen(
        state = state,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun ProfileScreen(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppScaffoldControllerEffect(
        appBarState = AppBarState.Custom(key = PROFILE_SCREEN_ROUTE_PATH) {
            when (state.profileState) {
                is State.Profile.Loaded -> {
                    ProfileAppBar(
                        fullName = state.profileState.fullName,
                        avatar = state.profileState.avatar,
                        onGoBackClick = { onIntent(Intent.OnGoBackClick) },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
                State.Profile.Loading -> {
                    ProfileAppBarShimmer(
                        onGoBackClick = { onIntent(Intent.OnGoBackClick) },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                }
            }
        }
    )

    AvatarSourceBottomSheet(
        state = state,
        onIntent = onIntent
    )

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProfileSettingsBlock(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .smallDeviceMaxWidth()
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun AvatarSourceBottomSheet(
    state: State,
    onIntent: (Intent) -> Unit,
) {
    AppSingleSelectBottomSheet(
        title = stringResource(R.string.profile_change_image_dialog_title),
        isShown = state.isAvatarSourceBottomSheetShown,
        items = AvatarSourceButton.entries
            .map { button -> stringResource(button.textRes) }
            .toImmutableList(),
        onClick = { index ->
            onIntent(
                Intent.OnAvatarSourceButtonClick(
                    AvatarSourceButton.entries[index]
                )
            )
        },
        onDismissRequest = { onIntent(Intent.OnAvatarSourceRequestDismiss) }
    )
}

@Composable
private fun ProfileSettingsBlock(
    state: State,
    onIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isAppInDarkTheme = isAppInDarkTheme()

    val buttonsState =
        if (state.isSignOutInProcess) AppButtonState.Disabled
        else AppButtonState.Idle

    val signOutButtonState =
        if (state.isSignOutInProcess) AppButtonState.Loading
        else AppButtonState.Idle

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(24.dp)
    ) {
        AppButton(
            text = stringResource(
                if (isAppInDarkTheme) {
                    R.string.profile_switch_to_light
                } else {
                    R.string.profile_switch_to_dark
                }
            ),
            buttonState = buttonsState,
            onClick = {
                onIntent(
                    Intent.OnSwitchUiModeButtonClick(
                        toDarkTheme = !isAppInDarkTheme
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_mother_language),
            buttonState = buttonsState,
            onClick = { onIntent(Intent.OnChangeLanguageButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_image),
            buttonState = buttonsState,
            onClick = { onIntent(Intent.OnChangeAvatarButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )

        AppButton(
            text = stringResource(R.string.profile_change_logout),
            buttonState = signOutButtonState,
            buttonColors = AppButtonDefaults.colors(
                idleColor = AppTheme.colors.error
            ),
            onClick = { onIntent(Intent.OnLogoutButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ScreenSideEffects(
    sideEffects: Flow<SideEffect>,
    onIntent: (Intent) -> Unit,
) {
    val context = LocalContext.current
    val snackbarHostState = LocalAppScaffoldController.current.snackbarHostState
    var takePhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            context.grantReadUriPermission(uri)
            onIntent(Intent.OnAvatarUriReceived(uri))
        }
    }

    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && takePhotoUri != Uri.EMPTY) {
            onIntent(Intent.OnAvatarUriReceived(takePhotoUri))
        }
    }

    val cameraPermissionLauncher = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                takePhotoUri = context.createTempPictureUri(
                    authority = "${ProfileComponent.environmentConfig.applicationId}.provider"
                )

                takePhoto.launch(takePhotoUri)
            }
        }
    )

    ScreenSideEffectsFlowHandler(sideEffects = sideEffects) { sideEffect ->
        when (sideEffect) {
            is SideEffect.Message -> {
                handleMessageSideEffect(
                    message = sideEffect.text,
                    snackbarHostState = snackbarHostState,
                    context = context,
                )
            }
            SideEffect.PickPhotoFromGallery -> {
                photoPicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
            SideEffect.TakePhoto -> {
                cameraPermissionLauncher.launchPermissionRequest()
            }
        }
    }
}

private class ProfileScreenPreviewStateProvider : PreviewParameterProvider<State> {
    override val values = sequenceOf(
        State(profileState = State.Profile.Loading),
        State(
            profileState = State.Profile.Loaded(
                fullName = strRes("Ivan Ivanov"),
                avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
            )
        ),
        State(
            profileState = State.Profile.Loaded(
                fullName = strRes("Ivan Ivanov"),
                avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
            ),
            isSignOutInProcess = true
        )
    )
}

@Composable
private fun ProfileScreenPreview(state: State) {
    AppRootContainer { _, _ ->
        ProfileScreen(
            state = state,
            onIntent = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileScreenPreviewLight(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ProfileScreenPreviewDark(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
)
private fun ProfileScreenPreviewLightLarge(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}

@Composable
@Preview(
    showBackground = true,
    device = COMPOSE_LARGE_DEVICE_SPEC,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun ProfileScreenPreviewDarkLarge(
    @PreviewParameter(ProfileScreenPreviewStateProvider::class) state: State
) {
    ProfileScreenPreview(state)
}
