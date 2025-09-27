@file:OptIn(ExperimentalWasmJsInterop::class)

package permission

import kotlin.js.ExperimentalWasmJsInterop
import permission.exception.PermissionDeniedException
import permission.model.PermissionStateType
import permission.model.PermissionType
import type.Object
import type.toJsString
import kotlinx.coroutines.await
import permission.permissions
import web.navigator.navigator
import web.permissions.PermissionState

private val PERMISSION_TYPES = listOf(
    listOf(PermissionType.CAMERA) to listOf("camera"),
    listOf(PermissionType.RECORD_AUDIO) to listOf("microphone"),
    listOf(
        PermissionType.LOCATION,
        PermissionType.COARSE_LOCATION,
        PermissionType.BACKGROUND_LOCATION,
    ) to listOf("geolocation"),
    listOf(PermissionType.REMOTE_NOTIFICATION) to listOf("push", "notifications"),
    listOf(PermissionType.WRITE_STORAGE) to listOf("persistent-storage"),
    listOf(PermissionType.STORAGE, PermissionType.GALLERY) to
        listOf(
            "storage-access",
            "top-level-storage-access",
        ),
    listOf(PermissionType.LOCAL_FONTS) to listOf("local-fonts"),
    listOf(PermissionType.SENSORS) to listOf("accelerometer", "gyroscope", "magnetometer", "ambient-light-sensor"),
    listOf(PermissionType.BACKGROUND_SYNC) to listOf("background-sync"),
    listOf(PermissionType.COMPUTE_PRESSURE) to listOf("compute-pressure"),
    listOf(PermissionType.ACCESSIBILITY_EVENTS) to listOf("accessibility-events"),
    listOf(PermissionType.CLIPBOARD_READ) to listOf("clipboard-read"),
    listOf(PermissionType.CLIPBOARD_WRITE) to listOf("clipboard-write"),
    listOf(PermissionType.MIDI) to listOf("midi"),
    listOf(PermissionType.PAYMENT_HANDLER) to listOf("payment-handler"),
    listOf(PermissionType.SCREEN_WAKE_LOCK) to listOf("screen-wake-lock"),
    listOf(PermissionType.WINDOW_MANAGEMENT) to listOf("window-management"),
)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController {

    public actual suspend fun getPermissionState(permission: PermissionType): PermissionStateType =
        if (permissions()
                .await()
                .permissions
                .toList()
                .containsAll(permission.toPlatformPermission()!!)
        ) {
            PermissionStateType.GRANTED
        }
        else {
            PermissionStateType.NOT_DETERMINED
        }

    public actual suspend fun getPermissions(permission: PermissionType) {
        if (!permission
                .toPlatformPermission()!!
                .all {
                    navigator.permissions
                        .query(
                            Object {
                                name = it.toJsString()
                            },
                        ).state == PermissionState.granted
                }
        ) {
            throw PermissionDeniedException("Permission denied")
        }
    }

    public actual fun openAppSettings(): Unit = Unit
}

private fun PermissionType.toPlatformPermission(): List<String>? =
    PERMISSION_TYPES.find { it.first.contains(this) }?.second
