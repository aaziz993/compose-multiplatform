package klib.data.permission

import io.ktor.utils.io.CancellationException
import java.awt.Desktop
import klib.data.permission.exception.PermissionDeniedAlwaysException
import klib.data.permission.exception.PermissionDeniedException
import klib.data.permission.exception.PermissionRequestCanceledException
import klib.data.permission.model.Permission
import klib.data.permission.model.PermissionState
import kotlin.Throws

public actual class PermissionsController {

    public actual suspend fun getPermissionState(permission: Permission): PermissionState =
        PermissionState.GRANTED

    @Throws(CancellationException::class, PermissionDeniedAlwaysException::class, PermissionDeniedException::class, PermissionRequestCanceledException::class)
    public actual suspend fun providePermission(permission: Permission): Unit = Unit

    public actual fun openAppSettings(): Unit = Desktop.getDesktop().openHelpViewer()
}
