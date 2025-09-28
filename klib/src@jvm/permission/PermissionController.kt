package permission

import permission.model.Permission
import permission.model.PermissionStateType

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController {
    public actual suspend fun getPermissionState(permission: Permission): PermissionStateType =
        PermissionStateType.GRANTED

    public actual suspend fun getPermissions(permission: Permission): Unit = Unit

    public actual fun openAppSettings(): Unit = Unit
}
