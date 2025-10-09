package klib.data.permission

import klib.data.permission.model.Permission
import klib.data.permission.model.PermissionState

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionsController {
    public actual suspend fun getPermissionState(permission: Permission): PermissionState =
        PermissionState.GRANTED

    public actual suspend fun providePermission(permission: Permission): Unit = Unit

    public actual fun openAppSettings(): Unit = Unit
}
