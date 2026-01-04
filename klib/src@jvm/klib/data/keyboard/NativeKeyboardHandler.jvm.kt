package klib.data.keyboard

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.awt.AWTException
import java.awt.Robot
import java.awt.Toolkit
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger
import klib.data.keyboard.model.KEYS
import klib.data.keyboard.model.Key
import klib.data.keyboard.model.KeyEvent
import klib.data.keyboard.model.KeyState
import klib.data.keyboard.model.toKey

internal object JvmKeyboardHandler : NativeKeyboardHandlerBase(), NativeKeyListener {

    private val pressedKeys = ConcurrentHashMap.newKeySet<Key>()

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

    override fun nativeKeyPressed(nativeEvent: NativeKeyEvent) {
        val key = nativeEvent.toKey()
        if (key != Key.Unknown && pressedKeys.add(key)) emit(key, KeyState.KeyDown)
    }

    override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
        val key = nativeEvent.toKey()
        if (key != Key.Unknown && pressedKeys.remove(key)) emit(key, KeyState.KeyUp)
    }

    override fun sendEvent(event: KeyEvent) {
        if (event.key == Key.Unknown) return

        if (event.state == KeyState.KeyDown) robot.keyPress(KEYS[event.key]!!)
        else robot.keyRelease(KEYS[event.key]!!)
    }

    override fun getKeyState(key: Key): KeyState {
        if (key == Key.Unknown) return KeyState.KeyUp

        return if (key in pressedKeys) KeyState.KeyDown else KeyState.KeyUp
    }

    override fun isCapsLockOn(): Boolean =
        Toolkit.getDefaultToolkit()
            .getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)

    override fun isNumLockOn(): Boolean =
        Toolkit.getDefaultToolkit()
            .getLockingKeyState(java.awt.event.KeyEvent.VK_NUM_LOCK)

    override fun isScrollLockOn(): Boolean =
        Toolkit.getDefaultToolkit()
            .getLockingKeyState(java.awt.event.KeyEvent.VK_SCROLL_LOCK)

    override fun start(): Unit = GlobalScreen.addNativeKeyListener(this)

    override fun stop() {
        GlobalScreen.removeNativeKeyListener(this)
        pressedKeys.clear()
    }
}

public actual fun nativeKeyboardHandlerForPlatform(): NativeKeyboardHandler = JvmKeyboardHandler
