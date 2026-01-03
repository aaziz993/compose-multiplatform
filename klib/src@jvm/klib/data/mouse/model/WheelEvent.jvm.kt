package klib.data.mouse.model

import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent
import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf

internal val WHEEL_EVENTS: BiMap<WheelMode, Int> = biMapOf(
    WheelMode.LINE to NativeMouseWheelEvent.WHEEL_UNIT_SCROLL,
    WheelMode.PAGE to NativeMouseWheelEvent.WHEEL_BLOCK_SCROLL,
)
