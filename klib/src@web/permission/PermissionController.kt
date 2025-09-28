@file:OptIn(ExperimentalWasmJsInterop::class)

package permission

import js.objects.unsafeJso
import kotlin.js.ExperimentalWasmJsInterop
import kotlinx.coroutines.await
import permission.exception.PermissionDeniedException
import permission.model.PermissionState
import permission.model.Permission
import permission.permissions
import web.navigator.navigator
import web.permissions.PermissionName
import web.permissions.PermissionState as WebPermissionState
import web.permissions.camera
import web.permissions.geolocation
import web.permissions.granted
import web.permissions.microphone
import web.permissions.midi
import web.permissions.notifications
import web.permissions.persistentStorage
import web.permissions.push
import web.permissions.query
import web.permissions.storageAccess

private val PERMISSIONS = listOf(
    listOf(Permission.CAMERA) to listOf(PermissionName.camera),
    listOf(Permission.RECORD_AUDIO) to listOf(PermissionName.microphone),
    listOf(
        Permission.LOCATION,
        Permission.COARSE_LOCATION,
        Permission.BACKGROUND_LOCATION,
    ) to listOf(PermissionName.geolocation),
    listOf(Permission.REMOTE_NOTIFICATION) to listOf(PermissionName.push, PermissionName.notifications),
    listOf(Permission.WRITE_STORAGE) to listOf(PermissionName.persistentStorage),
    listOf(Permission.STORAGE, Permission.GALLERY) to
        listOf(
            PermissionName.storageAccess,
            PermissionName.topLevelStorageAccess,
        ),
    listOf(Permission.LOCAL_FONTS) to listOf(PermissionName.localFonts),
    listOf(Permission.SENSORS) to listOf(PermissionName.accelerometer, PermissionName.gyroscope, PermissionName.magnetometer, PermissionName.ambientLightSensor),
    listOf(Permission.BACKGROUND_SYNC) to listOf(PermissionName.backgroundSync),
    listOf(Permission.COMPUTE_PRESSURE) to listOf(PermissionName.computePressure),
    listOf(Permission.ACCESSIBILITY_EVENTS) to listOf(PermissionName.accessibilityEvents),
    listOf(Permission.CLIPBOARD_READ) to listOf(PermissionName.clipboardRead),
    listOf(Permission.CLIPBOARD_WRITE) to listOf(PermissionName.clipboardWrite),
    listOf(Permission.MIDI) to listOf(PermissionName.midi),
    listOf(Permission.PAYMENT_HANDLER) to listOf(PermissionName.paymentHandler),
    listOf(Permission.SCREEN_WAKE_LOCK) to listOf(PermissionName.screenWakeLock),
    listOf(Permission.WINDOW_MANAGEMENT) to listOf(PermissionName.windowManagement),
)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController {

    public actual suspend fun getPermissionState(permission: Permission): PermissionState =
        if (permissions()
                .await()
                .permissions
                .toList()
                .containsAll(permission.toPlatformPermission()!!)
        ) PermissionState.GRANTED
        else PermissionState.NOT_DETERMINED

    public actual suspend fun getPermissions(permission: Permission) {
        if (!permission
                .toPlatformPermission()!!
                .all { permission ->
                    navigator.permissions
                        .query(
                            unsafeJso {
                                name = permission
                            },
                        ).state == WebPermissionState.granted
                }
        ) throw PermissionDeniedException("Permission denied")
    }

    public actual fun openAppSettings(): Unit = Unit
}

private fun Permission.toPlatformPermission(): List<PermissionName>? =
    PERMISSIONS.find { it.first.contains(this) }?.second
