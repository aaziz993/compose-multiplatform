package clib.permission

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import klib.permission.PermissionController
import klib.permission.PermissionControllerFactory

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory {
    val context: Context = LocalContext.current
    return remember(context) {
        PermissionControllerFactory {
            PermissionController(context.applicationContext)
        }
    }
}
