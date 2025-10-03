package clib.data.permission

import androidx.compose.runtime.Composable
import clib.data.permission.PermissionControllerFactory
import klib.data.permission.PermissionController

@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { PermissionController() }
