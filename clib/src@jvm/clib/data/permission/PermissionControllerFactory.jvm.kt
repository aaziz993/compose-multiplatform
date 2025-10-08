package clib.data.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionsController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { PermissionsController() }
