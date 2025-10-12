package ui.auth.verification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraFront
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.navigation.viewmodel.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.camera
import compose_app.generated.resources.confirm
import compose_app.generated.resources.upload_id
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import presentation.components.tooltipbox.AppTooltipBox
import ui.auth.verification.presentation.viewmodel.VerificationAction
import ui.auth.verification.presentation.viewmodel.VerificationState
import ui.navigation.presentation.Services
import ui.navigation.presentation.Verification

@Composable
public fun VerificationScreen(
    modifier: Modifier = Modifier,
    route: Verification = Verification,
    onAuthAction: (AuthAction) -> Unit = {},
    state: VerificationState = VerificationState(),
    onAction: (VerificationAction) -> Unit = {},
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    // Initialize MOKO Permissions controller
    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.upload_id),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            state.idImage?.let { Text("Document selected", color = Color.Black) }
                ?: Text("No document selected", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    // Call your multiplatform file picker here
                    // FilePicker.pickFile()
                    onAction(VerificationAction.SetIdImage(""))
                }
            },
        ) {
            Text("Select from files")
        }

        AppTooltipBox(stringResource(Res.string.camera)) {
            IconButton(
                onClick = {
                    onAction(VerificationAction.SetUserImage(""))
                },
            ) {
                Icon(imageVector = Icons.Default.CameraFront, contentDescription = stringResource(Res.string.camera))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onNavigationAction(NavigationAction.TypeNavigation.Navigate(Services))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.confirm))
        }
    }
}
