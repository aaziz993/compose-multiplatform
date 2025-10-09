package clib.data.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionsController

public fun interface PermissionsControllerFactory {
    public fun createPermissionsController(): PermissionsController
}

@Composable
public expect fun rememberPermissionsControllerFactory(): PermissionsControllerFactory
