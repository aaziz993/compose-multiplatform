package klib.data.keyboard

import klib.data.keyboard.model.Key
import klib.data.keyboard.model.KeyEvent
import klib.data.keyboard.model.KeyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * A low-level implementation for handling [KeyEvent]s (sending and receiving).
 */
public interface NativeKeyboardHandler {

    /**
     * A [SharedFlow] of [KeyEvent] for receiving Key events from the target platform.
     */
    public val events: SharedFlow<KeyEvent>

    /**
     * Sends the [KeyEvent] to the host.
     */
    public fun sendEvent(event: KeyEvent)

    /**
     * Gets the current key state (if its pressed or not) from the host.
     *
     * For toggle states consider using [isCapsLockOn], [isNumLockOn] and [isScrollLockOn] for respective purpose.
     */
    public fun getKeyState(key: Key): KeyState

    /**
     * Returns true if [Key.CapsLock] is toggled to be on.
     */
    public fun isCapsLockOn(): Boolean

    /**
     * Returns true if [Key.NumLock] is toggled to be on.
     */
    public fun isNumLockOn(): Boolean

    /**
     * Returns true if [Key.ScrollLock] is toggled to be on.
     */
    public fun isScrollLockOn(): Boolean
}

/**
 * Gets the [NativeKeyboardHandler] for the particular platform.
 * Always returns the same instance.
 */
public expect fun nativeKeyboardHandlerForPlatform(): NativeKeyboardHandler

internal abstract class NativeKeyboardHandlerBase : NativeKeyboardHandler {

    protected val eventsInternal: MutableSharedFlow<KeyEvent> = MutableSharedFlow(extraBufferCapacity = 8)
    protected val unconfinedScope = CoroutineScope(Dispatchers.Unconfined)

    override val events: SharedFlow<KeyEvent>
        get() = eventsInternal.asSharedFlow()

    init {
        // When subscriptionCount increments from 0 to 1, setup the native hook.
        eventsInternal.subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .drop(1) // Drop first false event
            .onEach { if (it) startReadingEvents() else stopReadingEvents() }
            .launchIn(unconfinedScope)
    }

    protected abstract fun startReadingEvents()
    protected abstract fun stopReadingEvents()

    protected fun emit(key: Key, state: KeyState) {
        eventsInternal.tryEmit(KeyEvent(key, state))
    }
}
