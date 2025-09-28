package permission

import permission.exception.PermissionDeniedAlwaysException
import permission.exception.PermissionDeniedException
import permission.exception.PermissionRequestCanceledException
import permission.exception.PermissionUnsupportedException
import permission.model.PermissionStateType
import permission.model.Permission
import dev.icerock.moko.permissions.Permission as MokoPermission
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException

private val PERMISSION_STATES = mapOf(
    PermissionState.NotDetermined to PermissionStateType.NOT_DETERMINED,
    PermissionState.NotGranted to PermissionStateType.NOT_GRANTED,
    PermissionState.Granted to PermissionStateType.GRANTED,
    PermissionState.Denied to PermissionStateType.DENIED,
    PermissionState.DeniedAlways to PermissionStateType.DENIED_ALWAYS,
)

private val PERMISSIONS: Map<Permission, Permission> =
    mapOf(
        Permission.CAMERA to MokoPermission.CAMERA,
        Permission.GALLERY to MokoPermission.GALLERY,
        Permission.STORAGE to MokoPermission.STORAGE,
        Permission.WRITE_STORAGE to MokoPermission.WRITE_STORAGE,
        Permission.LOCATION to MokoPermission.LOCATION,
        Permission.COARSE_LOCATION to MokoPermission.COARSE_LOCATION,
        Permission.BACKGROUND_LOCATION to MokoPermission.BACKGROUND_LOCATION,
        Permission.BLUETOOTH_LE to MokoPermission.BLUETOOTH_LE,
        Permission.REMOTE_NOTIFICATION to MokoPermission.REMOTE_NOTIFICATION,
        Permission.RECORD_AUDIO to MokoPermission.RECORD_AUDIO,
        Permission.BLUETOOTH_SCAN to MokoPermission.BLUETOOTH_SCAN,
        Permission.BLUETOOTH_ADVERTISE to MokoPermission.BLUETOOTH_ADVERTISE,
        Permission.BLUETOOTH_CONNECT to MokoPermission.BLUETOOTH_CONNECT,
        Permission.CONTACTS to MokoPermission.CONTACTS,
        Permission.SENSORS to MokoPermission.MOTION,
    )

public abstract class PermissionControllerImpl(
    protected val permissionsController: PermissionsController,
) {

    public suspend fun getPermissionState(permission: Permission): PermissionStateType =
        PERMISSIONS[permission]?.let { PERMISSION_STATES[permissionsController.getPermissionState(it)]!! }
            ?: PermissionStateType.UNSUPPORTED

    public suspend fun getPermissions(permission: Permission) {
        PERMISSIONS[permission]?.let {
            try {
                permissionsController.providePermission(it)
            }
            catch (e: DeniedAlwaysException) {
                throw PermissionDeniedAlwaysException(e.message)
            }
            catch (e: DeniedException) {
                PermissionDeniedException(e.message)
            }
            catch (e: RequestCanceledException) {
                PermissionRequestCanceledException(e.message)
            }
        } ?: PermissionUnsupportedException
    }

    public fun openAppSettings(): Unit = permissionsController.openAppSettings()
}
