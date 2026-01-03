package klib.data.mouse.model

import klib.data.type.collections.bimap.biMapOf
import web.mouse.WheelEvent

internal val WHEEL_EVENTS = biMapOf(
    WheelMode.PIXEL to WheelEvent.DOM_DELTA_PIXEL,
    WheelMode.LINE to WheelEvent.DOM_DELTA_LINE,
    WheelMode.PAGE to WheelEvent.DOM_DELTA_PAGE,
)
