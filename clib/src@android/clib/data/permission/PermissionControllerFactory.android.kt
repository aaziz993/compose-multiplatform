package clib.data.permission

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import klib.data.permission.PermissionsController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory {
    val context: Context = LocalContext.current
    return remember(context) {
        PermissionControllerFactory {
            PermissionsController(context.applicationContext)
        }
    }
}
