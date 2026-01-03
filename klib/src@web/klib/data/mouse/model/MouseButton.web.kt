package klib.data.mouse.model

import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.biMapOf
import web.mouse.AUXILIARY
import web.mouse.FIFTH
import web.mouse.FOURTH
import web.mouse.MAIN
import web.mouse.SECONDARY
import web.mouse.MouseButton as WebMouseButton

internal val MOUSE_BUTTONS: BiMap<Button, WebMouseButton> = biMapOf(
    Button.Left to WebMouseButton.MAIN,
    Button.Middle to WebMouseButton.AUXILIARY,
    Button.Right to WebMouseButton.SECONDARY,
    Button.Back to WebMouseButton.FOURTH,
    Button.Forward to WebMouseButton.FIFTH,
)
