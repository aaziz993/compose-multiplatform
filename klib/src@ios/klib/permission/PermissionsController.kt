package klib.permission

import dev.icerock.moko.permissions.ios.PermissionsController
import klib.permission.PermissionControllerImpl

public actual class PermissionsController : PermissionControllerImpl(PermissionsController())
