package permission

import permission.model.PermissionStateType
import permission.model.Permission

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect class PermissionController {
    public suspend fun getPermissionState(permission: Permission): PermissionStateType

    public suspend fun getPermissions(permission: Permission)

    public fun openAppSettings()
}

public suspend fun PermissionController.isPermissionGranted(permission: Permission): Boolean =
    getPermissionState(permission) == PermissionStateType.GRANTED
