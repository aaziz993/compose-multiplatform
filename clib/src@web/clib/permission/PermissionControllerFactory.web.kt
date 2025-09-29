package clib.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import klib.permission.PermissionController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory {
    return remember {
        PermissionControllerFactory {
            PermissionController()
        }
    }
}
