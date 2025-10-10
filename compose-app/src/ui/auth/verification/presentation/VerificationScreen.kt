package ui.auth.verification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.data.permission.rememberPermissionsControllerFactory
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.locale.stringResource
import compose_app.generated.resources.Res
import compose_app.generated.resources.confirm
import compose_app.generated.resources.upload_id
import klib.data.type.auth.model.Auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.navigation.presentation.Verification

@Composable
public fun VerificationScreen(
    modifier: Modifier = Modifier,
    route: Verification = Verification,
    auth: Auth = Auth(),
    authAction: (AuthAction) -> Unit = {},
    navigationAction: (NavigationAction) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    // Initialize MOKO Permissions controller
    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    var selectedImage: Any? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = stringResource(Res.string.upload_id), style = MaterialTheme.typography.titleMedium)

        // Placeholder for image preview
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            selectedImage?.let {
                Text("Document selected", color = Color.Black)
            } ?: Text("No document selected", color = Color.Gray)
        }

        // Select from file
        Button(
            onClick = {
                coroutineScope.launch {
                    // Call your multiplatform file picker here
//                     selectedImage = FilePicker.pickFile()
                }
            },
        ) {
            Text("Select from files")
        }

        Button(
            onClick = {

            },
        ) {
            Text("Open camera")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { },
            enabled = selectedImage != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(Res.string.confirm))
        }
    }
}
