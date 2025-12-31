package clib.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import klib.permission.PermissionsController

@Composable
public actual fun rememberPermissionsControllerFactory(): PermissionsControllerFactory = remember {
    PermissionsControllerFactory { PermissionsController() }
}
