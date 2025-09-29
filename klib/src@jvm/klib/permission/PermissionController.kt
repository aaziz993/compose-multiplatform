package klib.permission

import klib.permission.model.Permission
import klib.permission.model.PermissionState

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController {
    public actual suspend fun getPermissionState(permission: Permission): PermissionState =
        PermissionState.GRANTED

    public actual suspend fun getPermissions(permission: Permission): Unit = Unit

    public actual fun openAppSettings(): Unit = Unit
}
