package klib.data.permission

import klib.data.permission.model.PermissionState
import klib.data.permission.model.Permission

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect class PermissionsController {

    public suspend fun getPermissionState(permission: Permission): PermissionState

    public suspend fun providePermission(permission: Permission)

    public fun openAppSettings()
}

public suspend fun PermissionsController.isPermissionGranted(permission: Permission): Boolean =
    getPermissionState(permission) == PermissionState.GRANTED
