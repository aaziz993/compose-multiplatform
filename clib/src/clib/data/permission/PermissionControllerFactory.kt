package clib.data.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionsController

public fun interface PermissionControllerFactory {
    public fun createPermissionsController(): PermissionsController
}

@Composable
public expect fun rememberPermissionControllerFactory(): PermissionControllerFactory
