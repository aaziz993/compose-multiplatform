package klib.data.mouse.model

import klib.data.keyboard.model.KeyState

public data class MouseEvent(
    val button: MouseButton = MouseButton.None,
    val state: KeyState = KeyState.KeyUp,
    val wheel: Int = 0,
    val x: Int = -1,
    val y: Int = -1,
)
