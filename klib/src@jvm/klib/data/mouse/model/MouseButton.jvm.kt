package klib.data.mouse.model

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf

internal val MOUSE_BUTTONS: BiMap<Button, Int> = biMapOf(
    Button.None to NativeMouseEvent.NOBUTTON,
    Button.Left to NativeMouseEvent.BUTTON1,
    Button.Middle to NativeMouseEvent.BUTTON2,
    Button.Right to NativeMouseEvent.BUTTON3,
    Button.Back to NativeMouseEvent.BUTTON4,
    Button.Forward to NativeMouseEvent.BUTTON5,
)
