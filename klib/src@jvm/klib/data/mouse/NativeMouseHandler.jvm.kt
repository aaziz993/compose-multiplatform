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
import klib.data.keyboard.model.KeyState
import klib.data.mouse.model.MOUSE_BUTTONS
import klib.data.mouse.model.MouseEvent

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

    override fun nativeMouseMoved(nativeEvent: NativeMouseEvent): Unit =
        emit(
            requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                "Unknown button ${nativeEvent.button}"
            },
            KeyState.KeyUp,
            0,
            nativeEvent.x,
            nativeEvent.y,
        )

    override fun nativeMouseDragged(nativeEvent: NativeMouseEvent): Unit =
        emit(
            requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                "Unknown button ${nativeEvent.button}"
            },
            KeyState.KeyDown,
            0,
            nativeEvent.x,
            nativeEvent.y,
        )

    override fun nativeMouseWheelMoved(nativeEvent: NativeMouseWheelEvent?) {
        nativeEvent?.let {
            emit(
                requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                    "Unknown button ${nativeEvent.button}"
                },
                KeyState.KeyUp,
                it.wheelDirection * it.wheelRotation,
                it.x,
                it.y,
            )
        }
    }

    override fun nativeMousePressed(nativeEvent: NativeMouseEvent): Unit =
        emit(
            requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                "Unknown button ${nativeEvent.button}"
            },
            KeyState.KeyDown,
            0,
            nativeEvent.x,
            nativeEvent.y,
        )

    override fun nativeMouseReleased(nativeEvent: NativeMouseEvent): Unit =
        emit(
            requireNotNull(MOUSE_BUTTONS.inverse[nativeEvent.button]) {
                "Unknown button ${nativeEvent.button}"
            },
            KeyState.KeyUp,
            0,
            nativeEvent.x,
            nativeEvent.y,
        )

    override fun sendEvent(event: MouseEvent) {
        if (event.x > -1 && event.y > -1) robot.mouseMove(event.x, event.y)

        if (event.wheel != 0) robot.mouseWheel(event.wheel)

        MOUSE_BUTTONS[event.button]?.let { button ->
            if (event.state == KeyState.KeyDown) robot.mousePress(button)
            else robot.mouseRelease(button)
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

