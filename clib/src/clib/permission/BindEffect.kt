package clib.permission

import androidx.compose.runtime.Composable
import klib.data.permission.PermissionController

@Suppress("FunctionName")
@Composable
public expect fun BindEffect(permissionController: PermissionController)
