package presentation.components.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import clib.presentation.components.dialog.ConfirmDialog
import compose_app.generated.resources.Res
import compose_app.generated.resources.cancel
import compose_app.generated.resources.confirm
import compose_app.generated.resources.sign_out
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeModifierMissing")
@Composable
public fun SignOutConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = onDismissRequest,
): Unit = ConfirmDialog(
    onDismissRequest = onCancel,
    onConfirm = onConfirm,
    icon = {
        Icon(
            imageVector = Icons.AutoMirrored.Default.Logout,
            contentDescription = stringResource(Res.string.sign_out),
            modifier = Modifier
                .size(64.dp)
                .padding(top = 16.dp)
                .padding(8.dp),
        )
    },
    title = {
        Text(
            text = stringResource(Res.string.sign_out),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
        )
    },
    cancelText = stringResource(Res.string.cancel),
    confirmText = stringResource(Res.string.confirm),
)
