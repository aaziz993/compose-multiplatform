package klib.data.keyboard

import klib.data.keyboard.model.Key
import klib.data.keyboard.model.KeyEvent
import klib.data.keyboard.model.KeyState
import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import klib.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.js.JsName
import kotlin.test.Test

class NativeKeyboardHandlerTest {

    @Test
    @JsName("Caps_lock_key_should_be_toggled_when_KeyDown_event_is_triggered")
    fun `Caps lock key should be toggled when KeyDown event is triggered`() {
        val handler = nativeKeyboardHandlerForPlatform()

        val initialState = handler.isCapsLockOn()

        handler.sendEvent(KeyEvent(Key.CapsLock, KeyState.KeyDown))
        handler.sendEvent(KeyEvent(Key.CapsLock, KeyState.KeyUp))

        val finalState = handler.isCapsLockOn()

        // Set the state back to initialState
        handler.sendEvent(KeyEvent(Key.CapsLock, KeyState.KeyDown))
        handler.sendEvent(KeyEvent(Key.CapsLock, KeyState.KeyUp))

        finalState shouldNotBeEqualComparingTo initialState
    }

    @Test
    @JsName("Test_send_and_receive_event")
    fun `Test send and receive event`() = runBlocking {
        val handler = nativeKeyboardHandlerForPlatform()

        launch {
            delay(5) // Make sure we didn't missed any event
            handler.sendEvent(KeyEvent(Key.LeftCtrl, KeyState.KeyDown))
            handler.sendEvent(KeyEvent(Key.LeftCtrl, KeyState.KeyUp))
        }

        val events = withTimeout(200) { handler.events.dropWhile { it.key != Key.LeftCtrl }.take(2).toList() }

        events[0] should {
            it.key shouldBe Key.LeftCtrl
            it.state shouldBe KeyState.KeyDown
        }
        events[1] should {
            it.key shouldBe Key.LeftCtrl
            it.state shouldBe KeyState.KeyUp
        }
    }
}
