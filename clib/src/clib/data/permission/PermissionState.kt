package clib.data.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.permission.PermissionsController
import klib.data.permission.isPermissionGranted
import klib.data.permission.model.Permission
import klib.data.type.collections.replaceWith

@Suppress("ComposeCompositionLocalUsage")
public val LocalPermissionsState: ProvidableCompositionLocal<PermissionsState> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalComponentsState") }

public class PermissionsState(private val delegate: PermissionsController) {

    public val permissions: Set<Permission>
        field = mutableStateSetOf()

    public suspend fun providePermission(permission: Permission?) {
        if (permission != null) delegate.providePermission(permission)
        update()
    }

    public fun openAppSettings(): Unit = delegate.openAppSettings()

    internal suspend fun update() {
        permissions.replaceWith(
            Permission.entries.filter { delegate.isPermissionGranted(it) }.toSet(),
        )
    }
}

@Composable
public fun rememberPermissionsState(): PermissionsState {
    val permissionFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    BindEffect(permissionsController)

    val permissionsState = remember(permissionsController) {
        PermissionsState(permissionsController)
    }

    LaunchedEffect(permissionsState) {
        permissionsState.update()
    }

    return permissionsState
}
