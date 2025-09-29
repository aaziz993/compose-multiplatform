package clib.permission

import androidx.compose.runtime.Composable
import klib.permission.PermissionController
import klib.permission.PermissionControllerFactory

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { PermissionController() }
