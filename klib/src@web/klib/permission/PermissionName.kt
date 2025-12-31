package klib.permission

import js.reflect.unsafeCast
import web.permissions.PermissionName

public inline val PermissionName.Companion.topLevelStorageAccess: PermissionName
    get() = unsafeCast("top-level-storage-access")

public inline val PermissionName.Companion.localFonts: PermissionName
    get() = unsafeCast("local-fonts")

public inline val PermissionName.Companion.accelerometer: PermissionName
    get() = unsafeCast("accelerometer")

public inline val PermissionName.Companion.gyroscope: PermissionName
    get() = unsafeCast("gyroscope")

public inline val PermissionName.Companion.magnetometer: PermissionName
    get() = unsafeCast("magnetometer")

public inline val PermissionName.Companion.ambientLightSensor: PermissionName
    get() = unsafeCast("ambient-light-sensor")

public inline val PermissionName.Companion.backgroundSync: PermissionName
    get() = unsafeCast("background-sync")

public inline val PermissionName.Companion.computePressure: PermissionName
    get() = unsafeCast("compute-pressure")

public inline val PermissionName.Companion.accessibilityEvents: PermissionName
    get() = unsafeCast("accessibility-events")

public inline val PermissionName.Companion.clipboardRead: PermissionName
    get() = unsafeCast("clipboard-read")

public inline val PermissionName.Companion.clipboardWrite: PermissionName
    get() = unsafeCast("clipboard-write")

public inline val PermissionName.Companion.paymentHandler: PermissionName
    get() = unsafeCast("payment-handler")

public inline val PermissionName.Companion.screenWakeLock: PermissionName
    get() = unsafeCast("screen-wake-lock")

public inline val PermissionName.Companion.windowManagement: PermissionName
    get() = unsafeCast("window-management")

