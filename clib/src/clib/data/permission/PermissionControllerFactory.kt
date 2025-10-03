package clib.data.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionController

public fun interface PermissionControllerFactory {
    public fun createPermissionsController(): PermissionController
}

@Composable
public expect fun rememberPermissionControllerFactory(): PermissionControllerFactory
