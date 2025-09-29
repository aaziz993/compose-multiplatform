package klib.permission

import dev.icerock.moko.permissions.ios.PermissionsController
import klib.permission.PermissionControllerImpl

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual class PermissionController : PermissionControllerImpl(PermissionsController())
