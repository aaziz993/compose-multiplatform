package clib.data.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import klib.data.permission.PermissionsController
import klib.data.permission.isPermissionGranted
import klib.data.permission.model.Permission

@Composable
public fun rememberPermissions(permissionsController: PermissionsController): Set<Permission> {
    val grantedPermissions = remember { mutableStateOf(setOf<Permission>()) }

    LaunchedEffect(Unit) {
        val allGranted = Permission.entries.filter { permission ->
            permissionsController.isPermissionGranted(permission)
        }.toSet()
        grantedPermissions.value = allGranted
    }

    return grantedPermissions.value
}
