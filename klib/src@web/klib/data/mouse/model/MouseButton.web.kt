package klib.data.mouse.model

import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf
import web.mouse.AUXILIARY
import web.mouse.FIFTH
import web.mouse.FOURTH
import web.mouse.MAIN
import web.mouse.SECONDARY
import web.mouse.MouseButton as WebMouseButton

internal val MOUSE_BUTTONS: BiMap<MouseButton, WebMouseButton> = biMapOf(
    MouseButton.Left to WebMouseButton.MAIN,
    MouseButton.Middle to WebMouseButton.AUXILIARY,
    MouseButton.Right to WebMouseButton.SECONDARY,
    MouseButton.Back to WebMouseButton.FOURTH,
    MouseButton.Forward to WebMouseButton.FIFTH,
)
