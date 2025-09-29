package klib.permission

import klib.permission.model.PermissionState
import klib.permission.model.Permission

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect class PermissionController {
    public suspend fun getPermissionState(permission: Permission): PermissionState

    public suspend fun getPermissions(permission: Permission)

    public fun openAppSettings()
}

public suspend fun PermissionController.isPermissionGranted(permission: Permission): Boolean =
    getPermissionState(permission) == PermissionState.GRANTED
