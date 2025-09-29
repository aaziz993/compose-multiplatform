package clib.permission

import androidx.compose.runtime.Composable
import klib.permission.PermissionController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { PermissionController() }
