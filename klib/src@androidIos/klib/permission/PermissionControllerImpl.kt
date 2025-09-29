package klib.permission

import klib.permission.exception.PermissionDeniedAlwaysException
import klib.permission.exception.PermissionDeniedException
import klib.permission.exception.PermissionRequestCanceledException
import klib.permission.exception.PermissionUnsupportedException
import klib.permission.model.PermissionState
import klib.permission.model.Permission
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionState as MonoPermissionState
import dev.icerock.moko.permissions.Permission as MonoPermission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.bluetooth.BluetoothAdvertisePermission
import dev.icerock.moko.permissions.bluetooth.BluetoothConnectPermission
import dev.icerock.moko.permissions.bluetooth.BluetoothLEPermission
import dev.icerock.moko.permissions.bluetooth.BluetoothScanPermission
import dev.icerock.moko.permissions.camera.CameraPermission
import dev.icerock.moko.permissions.contacts.ContactPermission
import dev.icerock.moko.permissions.gallery.GalleryPermission
import dev.icerock.moko.permissions.location.BackgroundLocationPermission
import dev.icerock.moko.permissions.location.CoarseLocationPermission
import dev.icerock.moko.permissions.location.LocationPermission
import dev.icerock.moko.permissions.microphone.RecordAudioPermission
import dev.icerock.moko.permissions.motion.MotionPermission
import dev.icerock.moko.permissions.notifications.RemoteNotificationPermission
import dev.icerock.moko.permissions.storage.StoragePermission
import dev.icerock.moko.permissions.storage.WriteStoragePermission

private val PERMISSION_STATES = mapOf(
    MonoPermissionState.NotDetermined to PermissionState.NOT_DETERMINED,
    MonoPermissionState.NotGranted to PermissionState.NOT_GRANTED,
    MonoPermissionState.Granted to PermissionState.GRANTED,
    MonoPermissionState.Denied to PermissionState.DENIED,
    MonoPermissionState.DeniedAlways to PermissionState.DENIED_ALWAYS,
)

private val PERMISSIONS: Map<Permission, MonoPermission> =
    mapOf(
        Permission.CAMERA to CameraPermission,
        Permission.GALLERY to GalleryPermission,
        Permission.STORAGE to StoragePermission,
        Permission.WRITE_STORAGE to WriteStoragePermission,
        Permission.LOCATION to LocationPermission,
        Permission.COARSE_LOCATION to CoarseLocationPermission,
        Permission.BACKGROUND_LOCATION to BackgroundLocationPermission,
        Permission.BLUETOOTH_LE to BluetoothLEPermission,
        Permission.REMOTE_NOTIFICATION to RemoteNotificationPermission,
        Permission.RECORD_AUDIO to RecordAudioPermission,
        Permission.BLUETOOTH_SCAN to BluetoothScanPermission,
        Permission.BLUETOOTH_ADVERTISE to BluetoothAdvertisePermission,
        Permission.BLUETOOTH_CONNECT to BluetoothConnectPermission,
        Permission.CONTACT to ContactPermission,
        Permission.SENSORS to MotionPermission,
    )

public abstract class PermissionControllerImpl(
    protected val permissionsController: PermissionsController,
) {

    public suspend fun getPermissionState(permission: Permission): PermissionState =
        PERMISSIONS[permission]?.let { PERMISSION_STATES[permissionsController.getPermissionState(it)]!! }
            ?: PermissionState.UNSUPPORTED

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
