package clib.permission

import androidx.compose.runtime.Composable
import klib.permission.PermissionsController

@Suppress("FunctionName")
@Composable
public expect fun BindEffect(permissionsController: PermissionsController)
