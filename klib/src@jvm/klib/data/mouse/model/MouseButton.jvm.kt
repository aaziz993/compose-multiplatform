package klib.data.mouse.model

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf

internal val MOUSE_BUTTONS: BiMap<MouseButton, Int> = biMapOf(
    MouseButton.None to NativeMouseEvent.NOBUTTON,
    MouseButton.Left to NativeMouseEvent.BUTTON1,
    MouseButton.Middle to NativeMouseEvent.BUTTON2,
    MouseButton.Right to NativeMouseEvent.BUTTON3,
    MouseButton.Back to NativeMouseEvent.BUTTON4,
    MouseButton.Forward to NativeMouseEvent.BUTTON5,
)
