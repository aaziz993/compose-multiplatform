package clib.data.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import klib.data.permission.PermissionsController
import klib.data.permission.isPermissionGranted
import klib.data.permission.model.Permission

//@Suppress("ComposeCompositionLocalUsage")
//public val LocalPermissionsState: ProvidableCompositionLocal<PermissionsState> =
//    staticCompositionLocalOf { noLocalProvidedFor("LocalComponentsState") }
//
//public class PermissionsState(initialValue: Set<Permission> = emptySet()) {
//
//    public var permissions: MutableSet<Permission> = mutableStateSetOf<Permission>().apply { addAll(initialValue) }
//
//    public companion object Companion {
//
//        public val Saver: Saver<PermissionsState, *> = listSaver(
//            save = { listOf(it.permissions) },
//            restore = { PermissionsState(it[0]) },
//        )
//    }
//}
//
//@Composable
//public fun rememberPermissionsState(initialValue: Set<Permission> = emptySet()): PermissionsState {
//    val permissionsState = rememberSaveable(saver = PermissionsState.Saver) { PermissionsState(initialValue) }
//
//    LaunchedEffect(Unit) {
//        permissionsState.permissions.addAll(
//            Permission.entries.filter { permission ->
//                permissionsController.isPermissionGranted(permission)
//            },
//        )
//    }
//
//
//    return permissionsState
//}

@Composable
public fun rememberPermissions(permissionsController: PermissionsController): MutableSet<Permission> {
    val grantedPermissions = remember { mutableStateSetOf<Permission>() }

    LaunchedEffect(Unit) {
        grantedPermissions.addAll(
            Permission.entries.filter { permission ->
                permissionsController.isPermissionGranted(permission)
            },
        )
    }

    return grantedPermissions
}
