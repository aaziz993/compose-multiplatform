package clib.data.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import klib.data.permission.PermissionsController

@Composable
public actual fun rememberPermissionsControllerFactory(): PermissionsControllerFactory = remember {
    PermissionsControllerFactory { PermissionsController() }
}
