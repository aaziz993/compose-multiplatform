package klib.data.keyboard

import js.objects.unsafeJso
import klib.data.keyboard.model.KEYS
import klib.data.keyboard.model.Key
import klib.data.keyboard.model.KeyEvent
import klib.data.keyboard.model.KeyState
import web.events.addHandler
import web.keyboard.CapsLock
import web.keyboard.KEY_DOWN
import web.keyboard.KEY_UP
import web.keyboard.KeyboardEvent
import web.keyboard.ModifierKeyCode
import web.keyboard.NumLock
import web.keyboard.ScrollLock
import web.window.keyDownEvent
import web.window.keyUpEvent
import web.window.window

internal object WebKeyboardHandler : NativeKeyboardHandlerBase() {

    private val pressedKeys = mutableSetOf<Key>()

    private fun keyDown(e: KeyboardEvent) {
        val key = KEYS.inverse[e.code] ?: return
        if (key != Key.Unknown && pressedKeys.add(key)) emit(key, KeyState.KeyDown)
    }

    private fun keyUp(e: KeyboardEvent) {
        val key = KEYS.inverse[e.code] ?: return
        if (key != Key.Unknown && pressedKeys.remove(key)) emit(key, KeyState.KeyUp)
    }

    private var keyDownUnsubscribe: (() -> Unit)? = null
    private var keyUpUnsubscribe: (() -> Unit)? = null

    override fun startReadingEvents() {
        keyDownUnsubscribe = window.keyDownEvent.addHandler(::keyDown)
        keyUpUnsubscribe = window.keyUpEvent.addHandler(::keyUp)
    }

    override fun stopReadingEvents() {
        keyDownUnsubscribe?.invoke()
        keyUpUnsubscribe?.invoke()
        keyDownUnsubscribe = null
        keyUpUnsubscribe = null
        pressedKeys.clear()
    }

    override fun sendEvent(event: KeyEvent) {
        if (event.key == Key.Unknown) return

        val eventType = when (event.state) {
            KeyState.KeyDown -> KeyboardEvent.KEY_DOWN
            KeyState.KeyUp -> KeyboardEvent.KEY_UP
        }

        val keyboardEvent = KeyboardEvent(
            eventType,
            unsafeJso {
                code = requireNotNull(KEYS[event.key]) { "Unknown key '${event.key}'" }
                bubbles = true
                cancelable = true
            },
        )

        window.dispatchEvent(keyboardEvent)
    }

    override fun getKeyState(key: Key): KeyState =
        if (key in pressedKeys) KeyState.KeyDown else KeyState.KeyUp

    override fun isCapsLockOn(): Boolean =
        KeyboardEvent(KeyboardEvent.KEY_DOWN).getModifierState(ModifierKeyCode.CapsLock)

    override fun isNumLockOn(): Boolean =
        KeyboardEvent(KeyboardEvent.KEY_DOWN).getModifierState(ModifierKeyCode.NumLock)

    override fun isScrollLockOn(): Boolean =
        KeyboardEvent(KeyboardEvent.KEY_DOWN).getModifierState(ModifierKeyCode.ScrollLock)
}

public actual fun nativeKeyboardHandlerForPlatform(): NativeKeyboardHandler = WebKeyboardHandler
