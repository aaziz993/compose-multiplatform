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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.camera
import compose_app.generated.resources.confirm
import compose_app.generated.resources.not_selected
import compose_app.generated.resources.select
import compose_app.generated.resources.upload_id
import klib.data.auth.model.Auth
import kotlinx.coroutines.launch
import clib.data.type.primitives.string.stringResource
import presentation.components.tooltipbox.AppPlainTooltipBox
import ui.auth.verification.presentation.viewmodel.VerificationAction
import ui.auth.verification.presentation.viewmodel.VerificationState
import ui.navigation.presentation.Services
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
) {
    val coroutineScope = rememberCoroutineScope()



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
            state.idImage?.let { Text("", color = Color.Black) }
                ?: Text(stringResource(Res.string.not_selected), color = Color.Gray)
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
            Text(stringResource(Res.string.select))
        }

        IconButton(
            onClick = {
                onAction(VerificationAction.SetUserImage(""))
            },
        ) {
            AppPlainTooltipBox(tooltip = stringResource(Res.string.camera)) {
                Icon(Icons.Default.CameraAlt, stringResource(Res.string.camera))
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                onNavigationActions(
                    arrayOf(
                        NavigationAction.Push(Services),
                    ),
                )
            },
        ) {
            Text(text = stringResource(Res.string.confirm))
        }
    }
}
