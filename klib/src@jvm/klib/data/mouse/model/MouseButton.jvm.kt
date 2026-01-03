package klib.data.mouse.model

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import java.awt.event.InputEvent

internal val JNATIVE_MOUSE_BUTTONS: Map<Int, Button> = mapOf(
    NativeMouseEvent.BUTTON1 to Button.Left,
    NativeMouseEvent.BUTTON2 to Button.Middle,
    NativeMouseEvent.BUTTON3 to Button.Right,
    NativeMouseEvent.BUTTON4 to Button.Back,
    NativeMouseEvent.BUTTON5 to Button.Forward,
)

internal val MOUSE_BUTTONS: Map<Button, Int> = mapOf(
    Button.Left to InputEvent.BUTTON1_DOWN_MASK,
    Button.Middle to InputEvent.BUTTON2_DOWN_MASK,
    Button.Right to InputEvent.BUTTON3_DOWN_MASK,
)
