package clib.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { PermissionController() }
