package klib.data.mouse

import js.objects.unsafeJso
import klib.data.mouse.model.MOUSE_BUTTONS
import klib.data.mouse.model.MouseDown
import klib.data.mouse.model.MouseEvent
import klib.data.mouse.model.MouseMove
import klib.data.mouse.model.MouseUp
import klib.data.mouse.model.MouseWheel
import klib.data.mouse.model.WHEEL_EVENTS
import klib.data.type.reflection.trySet
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

    private fun mouseMoveHandler(nativeEvent: WebMouseEvent) {
        eventsInternal.tryEmit(
            MouseMove(
                nativeEvent.screenX,
                nativeEvent.screenY,
            ),
        )
    }

    private fun wheelHandler(nativeEvent: WheelEvent) {
        eventsInternal.tryEmit(
            MouseWheel(
                nativeEvent.deltaX,
                nativeEvent.deltaY,
                nativeEvent.deltaZ,
                requireNotNull(WHEEL_EVENTS.inverse[nativeEvent.deltaMode]) {
                    "Unknown wheel delta mode ${nativeEvent.deltaMode}"
                },
                nativeEvent.screenX,
                nativeEvent.screenY,
            ),
        )
    }

    private fun mouseDownHandler(nativeEvent: WebMouseEvent) {
        eventsInternal.tryEmit(
            MouseDown(
                requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                    "Unknown mouse button ${nativeEvent.button}"
                },
                nativeEvent.screenX,
                nativeEvent.screenY,
            ),
        )
    }

    private fun mouseUpHandler(nativeEvent: WebMouseEvent) {
        eventsInternal.tryEmit(
            MouseUp(
                requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                    "Unknown mouse button ${nativeEvent.button}"
                },
                nativeEvent.screenX,
                nativeEvent.screenY,
            ),
        )
    }

    private var mouseMoveHandlerUnsubscribe: (() -> Unit)? = null
    private var wheelHandlerUnsubscribe: (() -> Unit)? = null
    private var mouseDownHandlerUnsubscribe: (() -> Unit)? = null
    private var mouseUpHandlerUnsubscribe: (() -> Unit)? = null

    override fun start() {
        mouseMoveHandlerUnsubscribe = window.mouseMoveEvent.addHandler(::mouseMoveHandler)
        wheelHandlerUnsubscribe = window.wheelEvent.addHandler(::wheelHandler)
        mouseDownHandlerUnsubscribe = window.mouseDownEvent.addHandler(::mouseDownHandler)
        mouseUpHandlerUnsubscribe = window.mouseUpEvent.addHandler(::mouseUpHandler)
    }

    override fun stop() {
        mouseMoveHandlerUnsubscribe?.invoke()
        wheelHandlerUnsubscribe?.invoke()
        mouseDownHandlerUnsubscribe?.invoke()
        mouseUpHandlerUnsubscribe?.invoke()
        mouseMoveHandlerUnsubscribe = null
        wheelHandlerUnsubscribe = null
        mouseDownHandlerUnsubscribe = null
        mouseUpHandlerUnsubscribe = null
    }

    override fun sendEvent(event: MouseEvent) {
        when (event) {
            is MouseMove -> window.dispatchEvent(
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

            is MouseWheel -> window.dispatchEvent(
                WheelEvent(
                    WheelEvent.WHEEL,
                    unsafeJso {
                        ::deltaMode trySet WHEEL_EVENTS[event.mode]!!
                        ::deltaX trySet event.deltaX
                        ::deltaY trySet event.deltaY
                        ::deltaZ trySet event.deltaZ
                        bubbles = true
                        cancelable = true
                    },
                ),
            )

            is MouseDown -> window.dispatchEvent(
                WebMouseEvent(
                    WebMouseEvent.MOUSE_DOWN,
                    unsafeJso {
                        button = MOUSE_BUTTONS[event.button]!!
                        bubbles = true
                        cancelable = true
                    },
                ),
            )

            is MouseUp -> window.dispatchEvent(
                WebMouseEvent(
                    WebMouseEvent.MOUSE_UP,
                    unsafeJso {
                        button = MOUSE_BUTTONS[event.button]!!
                        bubbles = true
                        cancelable = true
                    },
                ),
            )
        }
    }
}

public actual fun nativeMouseHandlerForPlatform(): NativeMouseHandler = WebMouseHandler

