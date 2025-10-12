package clib.data.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import klib.data.permission.PermissionsController
import klib.data.permission.isPermissionGranted
import klib.data.permission.model.Permission

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
