package klib.data.permission

import io.ktor.utils.io.CancellationException
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.exception.PermissionRequestCanceledException
import klib.data.permission.model.PermissionState
import klib.data.permission.model.Permission

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
