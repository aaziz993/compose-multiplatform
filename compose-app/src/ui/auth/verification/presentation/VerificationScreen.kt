package ui.auth.verification.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import clib.data.type.primitives.string.stringResource
import clib.presentation.navigation.NavigationAction
import coil3.compose.rememberAsyncImagePainter
import compose_app.generated.resources.Res
import compose_app.generated.resources.camera
import compose_app.generated.resources.confirm
import compose_app.generated.resources.gallery
import compose_app.generated.resources.id
import compose_app.generated.resources.not_selected
import compose_app.generated.resources.user
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import klib.data.auth.model.Auth
import presentation.components.tooltipbox.AppPlainTooltipBox
import ui.auth.verification.presentation.viewmodel.VerificationAction
import ui.auth.verification.presentation.viewmodel.VerificationState
import ui.navigation.presentation.Verification

@Composable
public fun VerificationScreen(
    modifier: Modifier = Modifier,
    route: Verification = Verification,
    state: VerificationState = VerificationState(),
    onAction: (VerificationAction) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
): Unit = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val adaptiveInfo = currentWindowAdaptiveInfo()

        with(adaptiveInfo) {
            if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND))
                Row {
                    VerificationImagePicker(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp),
                        stringResource(Res.string.id),
                        state.idUri,
                    ) { value ->
                        onAction(VerificationAction.SetIdUri(value))
                    }

                    VerificationImagePicker(
                        Modifier
                            .weight(1f)
                            .padding(5.dp),
                        stringResource(Res.string.user),
                        state.userUri,
                    ) { value ->
                        onAction(VerificationAction.SetUserUri(value))
                    }
                }
            else Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                VerificationImagePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    stringResource(Res.string.id),
                    state.idUri,
                ) { value ->
                    onAction(VerificationAction.SetIdUri(value))
                }

                VerificationImagePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    stringResource(Res.string.user),
                    state.userUri,
                ) { value ->
                    onAction(VerificationAction.SetUserUri(value))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.idUri != null && state.userUri != null)
            Button(
                onClick = {
                    onAction(VerificationAction.Confirm)
                },
            ) {
                Text(text = stringResource(Res.string.confirm))
            }
    }
}

@Composable
private fun VerificationImagePicker(
    @Suppress("ComposeModifierWithoutDefault") modifier: Modifier,
    title: String,
    value: String?,
    onValueChange: (String) -> Unit,
) {
    var showCamera by remember { mutableStateOf(false) }
    if (showCamera) {
        ImagePickerLauncher(
            config = ImagePickerConfig(
                onPhotoCaptured = { result ->
                    onValueChange(result.uri)
                    showCamera = false
                },
                onError = { showCamera = false },
                onDismiss = { showCamera = false },
            ),
        )
    }

    var showGallery by remember { mutableStateOf(false) }
    if (showGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = { result ->
                onValueChange(result.single().uri)
                showGallery = false
            },
            onError = { showGallery = false },
            onDismiss = { showGallery = false },
            allowMultiple = false,
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            value?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                )
            } ?: Text(stringResource(Res.string.not_selected), color = Color.Gray)
        }

        Row {
            IconButton(
                onClick = {
                    showCamera = true
                },
            ) {
                AppPlainTooltipBox(tooltip = stringResource(Res.string.camera)) {
                    Icon(Icons.Default.CameraAlt, stringResource(Res.string.camera))
                }
            }
            IconButton(
                onClick = {
                    showGallery = true
                },
            ) {
                AppPlainTooltipBox(tooltip = stringResource(Res.string.gallery)) {
                    Icon(Icons.Default.BrowseGallery, stringResource(Res.string.gallery))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVerificationScreen(): Unit = VerificationScreen()
