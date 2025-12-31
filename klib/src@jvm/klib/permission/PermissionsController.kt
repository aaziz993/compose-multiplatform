package klib.permission

import io.ktor.utils.io.CancellationException
import klib.permission.exception.PermissionDeniedAlwaysException
import klib.permission.exception.PermissionDeniedException
import klib.permission.exception.PermissionRequestCanceledException
import klib.permission.model.Permission
import klib.permission.model.PermissionState
import kotlin.Throws

public actual class PermissionsController {

    public actual suspend fun getPermissionState(permission: Permission): PermissionState =
        PermissionState.GRANTED

    @Throws(CancellationException::class, PermissionDeniedAlwaysException::class, PermissionDeniedException::class, PermissionRequestCanceledException::class)
    public actual suspend fun providePermission(permission: Permission): Unit = Unit

    public actual fun openAppSettings(): Unit = Unit
}
