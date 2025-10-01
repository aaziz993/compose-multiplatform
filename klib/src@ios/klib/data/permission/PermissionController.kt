package klib.data.permission

import dev.icerock.moko.permissions.ios.PermissionsController
import klib.data.permission.PermissionControllerImpl

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController : PermissionControllerImpl(PermissionsController())
