package klib.permission

import io.ktor.utils.io.CancellationException
import klib.permission.exception.PermissionDeniedAlwaysException
import klib.permission.exception.PermissionDeniedException
import klib.permission.exception.PermissionRequestCanceledException
import klib.permission.model.PermissionState
import klib.permission.model.Permission

public expect class PermissionsController {

    public suspend fun getPermissionState(permission: Permission): PermissionState

    @Throws(
        CancellationException::class,
        PermissionDeniedAlwaysException::class,
        PermissionDeniedException::class,
        PermissionRequestCanceledException::class,
    )
    public suspend fun providePermission(permission: Permission)

    public fun openAppSettings()
}

public suspend fun PermissionsController.isPermissionGranted(permission: Permission): Boolean = getPermissionState(permission) == PermissionState.GRANTED
