package permission

import androidx.compose.runtime.Composable
@Composable
public actual fun rememberPermissionControllerFactory(): PermissionControllerFactory =
    PermissionControllerFactory { klib.permission.PermissionController() }
