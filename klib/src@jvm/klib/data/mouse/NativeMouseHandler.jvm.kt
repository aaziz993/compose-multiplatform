package klib.data.mouse

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener
import java.awt.AWTException
import java.awt.Robot
import java.util.logging.Level
import java.util.logging.Logger
import klib.data.mouse.model.JNATIVE_MOUSE_BUTTONS
import klib.data.mouse.model.MOUSE_BUTTONS
import klib.data.mouse.model.MouseDown
import klib.data.mouse.model.MouseEvent
import klib.data.mouse.model.MouseMove
import klib.data.mouse.model.MouseUp
import klib.data.mouse.model.MouseWheel
import klib.data.mouse.model.WHEEL_EVENTS
import kotlin.math.roundToInt

internal object JvmMouseHandler
    : NativeMouseHandlerBase(),
    NativeMouseMotionListener,
    NativeMouseWheelListener,
    NativeMouseInputListener {

    private var robot: Robot

    init {
        try {
            if (!GlobalScreen.isNativeHookRegistered()) GlobalScreen.registerNativeHook()
            Logger.getLogger(GlobalScreen::class.java.`package`.name).apply {
                level = Level.OFF
                useParentHandlers = false
            }
        }
        catch (e: NativeHookException) {
            error("Failed to register JNativeHook: ${e.message}")
        }

        try {
            robot = Robot()
        }
        catch (e: AWTException) {
            error("Failed to create Robot: ${e.message}")
        }
    }

    override fun nativeMouseMoved(nativeEvent: NativeMouseEvent) {
        eventsInternal.tryEmit(
            MouseMove(
                nativeEvent.x,
                nativeEvent.y,
            ),
        )
    }

    override fun nativeMouseWheelMoved(nativeEvent: NativeMouseWheelEvent?) {
        nativeEvent?.let {
            eventsInternal.tryEmit(
                MouseWheel(
                    if (it.wheelDirection == NativeMouseWheelEvent.WHEEL_HORIZONTAL_DIRECTION) it.wheelRotation.toDouble() else 0.0,
                    if (it.wheelDirection == NativeMouseWheelEvent.WHEEL_VERTICAL_DIRECTION) it.wheelRotation.toDouble() else 0.0,
                    null,
                    requireNotNull(WHEEL_EVENTS.inverse[nativeEvent.scrollType]) {
                        "Unknown scroll type ${nativeEvent.scrollType}"
                    },
                    it.x,
                    it.y,
                ),
            )
        }
    }

    override fun nativeMousePressed(nativeEvent: NativeMouseEvent) {
        eventsInternal.tryEmit(
            MouseDown(
                requireNotNull(JNATIVE_MOUSE_BUTTONS[nativeEvent.button]) {
                    "Unknown mouse button ${nativeEvent.button}"
                },
                nativeEvent.x,
                nativeEvent.y,
            ),
        )
    }

    override fun nativeMouseReleased(nativeEvent: NativeMouseEvent) {
        eventsInternal.tryEmit(
            MouseUp(
                requireNotNull(JNATIVE_MOUSE_BUTTONS[nativeEvent.button]) {
                    "Unknown mouse button ${nativeEvent.button}"
                },
                nativeEvent.x,
                nativeEvent.y,
            ),
        )
    }

    override fun sendEvent(event: MouseEvent) {
        when (event) {
            is MouseMove -> robot.mouseMove(event.x, event.y)
            is MouseWheel -> event.deltaY?.let { deltaY -> robot.mouseWheel(deltaY.roundToInt()) }
            is MouseDown -> robot.mousePress(MOUSE_BUTTONS[event.button]!!)
            is MouseUp -> robot.mouseRelease(MOUSE_BUTTONS[event.button]!!)
        }
    }

    override fun start() {
        GlobalScreen.addNativeMouseMotionListener(this)
        GlobalScreen.addNativeMouseWheelListener(this)
        GlobalScreen.addNativeMouseListener(this)
    }

    override fun stop() {
        GlobalScreen.removeNativeMouseMotionListener(this)
        GlobalScreen.removeNativeMouseWheelListener(this)
        GlobalScreen.removeNativeMouseListener(this)
    }
}

public actual fun nativeMouseHandlerForPlatform(): NativeMouseHandler = JvmMouseHandler

