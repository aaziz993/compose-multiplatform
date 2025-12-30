package klib.data.mouse

import klib.data.keyboard.model.KeyState
import klib.data.mouse.model.MouseButton
import klib.data.mouse.model.MouseEvent
import klib.data.type.collections.onHasSubscriptionChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A low-level implementation for handling [MouseEvent]s (sending and receiving).
 */
public interface NativeMouseHandler {

    /**
     * A [SharedFlow] of [MouseEvent] for receiving Key events from the target platform.
     */
    public val events: SharedFlow<MouseEvent>

    /**
     * Sends the [MouseEvent] to the host.
     */
    public fun sendEvent(event: MouseEvent)
}

/**
 * Gets the [NativeMouseHandler] for the particular platform.
 * Always returns the same instance.
 */
public expect fun nativeMouseHandlerForPlatform(): NativeMouseHandler

internal abstract class NativeMouseHandlerBase : NativeMouseHandler {

    protected val eventsInternal: MutableSharedFlow<MouseEvent> = MutableSharedFlow(extraBufferCapacity = 8)
    protected val unconfinedScope = CoroutineScope(Dispatchers.Unconfined)

    override val events: SharedFlow<MouseEvent>
        get() = eventsInternal.asSharedFlow()

    init {
        // When subscriptionCount increments from 0 to 1, setup the native hook.
        eventsInternal.onHasSubscriptionChange(
            { subscribed ->
                if (subscribed) start() else stop()
            },
            unconfinedScope,
        )
    }

    protected abstract fun start()
    protected abstract fun stop()

    protected fun emit(
        button: MouseButton,
        state: KeyState,
        wheel: Int,
        x: Int,
        y: Int
    ) {
        eventsInternal.tryEmit(MouseEvent(button, state, wheel, x, y))
    }
}

internal object DummyNativeMouseHandler : NativeMouseHandlerBase() {

    override fun sendEvent(event: MouseEvent): Unit = Unit
    override fun start(): Unit = Unit
    override fun stop(): Unit = Unit
}
