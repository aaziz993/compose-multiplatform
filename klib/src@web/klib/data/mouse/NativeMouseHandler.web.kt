package klib.data.mouse

import js.objects.unsafeJso
import klib.data.keyboard.model.KeyState
import klib.data.mouse.model.MOUSE_BUTTONS
import klib.data.mouse.model.MouseButton
import klib.data.mouse.model.MouseEvent
import klib.data.type.reflection.trySet
import kotlin.math.roundToInt
import web.events.addHandler
import web.mouse.MOUSE_DOWN
import web.mouse.MOUSE_MOVE
import web.mouse.MOUSE_UP
import web.mouse.WHEEL
import web.mouse.WheelEvent
import web.window.mouseDownEvent
import web.window.mouseMoveEvent
import web.window.mouseUpEvent
import web.window.wheelEvent
import web.window.window
import web.mouse.MouseEvent as WebMouseEvent

internal object WebMouseHandler : NativeMouseHandlerBase() {

    private fun mouseHandler(event: WebMouseEvent): Unit =
        emit(
            if (event.type == WebMouseEvent.MOUSE_DOWN || event.type == WebMouseEvent.MOUSE_UP)
                MOUSE_BUTTONS.inverse[event.button] ?: MouseButton.None
            else MouseButton.None,
            if (event.type == WebMouseEvent.MOUSE_DOWN) KeyState.KeyDown else KeyState.KeyUp,
            0,
            event.screenX,
            event.screenY,
        )

    private fun wheelHandler(e: WheelEvent): Unit =
        emit(
            MouseButton.None,
            KeyState.KeyUp,
            e.deltaY.roundToInt(),
            e.screenX,
            e.screenY,
        )

    private var mouseMoveUnsubscribe: (() -> Unit)? = null
    private var wheelUnsubscribe: (() -> Unit)? = null
    private var mouseDownUnsubscribe: (() -> Unit)? = null
    private var mouseUpUnsubscribe: (() -> Unit)? = null

    override fun startReadingEvents() {
        mouseMoveUnsubscribe = window.mouseMoveEvent.addHandler(::mouseHandler)
        wheelUnsubscribe = window.wheelEvent.addHandler(::wheelHandler)
        mouseDownUnsubscribe = window.mouseDownEvent.addHandler(::mouseHandler)
        mouseUpUnsubscribe = window.mouseUpEvent.addHandler(::mouseHandler)
    }

    override fun stopReadingEvents() {
        mouseMoveUnsubscribe?.invoke()
        wheelUnsubscribe?.invoke()
        mouseDownUnsubscribe?.invoke()
        mouseUpUnsubscribe?.invoke()
        mouseMoveUnsubscribe = null
        wheelUnsubscribe = null
        mouseDownUnsubscribe = null
        mouseUpUnsubscribe = null
    }

    override fun sendEvent(event: MouseEvent) {
        if (event.x > -1 && event.y > -1)
            window.dispatchEvent(
                WebMouseEvent(
                    WebMouseEvent.MOUSE_MOVE,
                    unsafeJso {
                        screenX = event.x
                        screenY = event.y
                        bubbles = true
                        cancelable = true
                    },
                ),
            )

        if (event.wheel != 0)
            window.dispatchEvent(
                WheelEvent(
                    WheelEvent.WHEEL,
                    unsafeJso {
                        ::screenX trySet event.x.takeIf { it > -1 }
                        ::screenY trySet event.y.takeIf { it > -1 }
                        deltaY = event.wheel.toDouble()
                        bubbles = true
                        cancelable = true
                    },
                ),
            )


        MOUSE_BUTTONS[event.button]?.let { button ->
            window.dispatchEvent(
                WebMouseEvent(
                    if (event.state == KeyState.KeyDown) WebMouseEvent.MOUSE_DOWN else WebMouseEvent.MOUSE_UP,
                    unsafeJso {
                        ::screenX trySet event.x.takeIf { it > -1 }
                        ::screenY trySet event.y.takeIf { it > -1 }
                        this.button = button
                        bubbles = true
                        cancelable = true
                    },
                ),
            )
        }
    }
}

public actual fun nativeMouseHandlerForPlatform(): NativeMouseHandler = WebMouseHandler

