package klib.data.mouse

import klib.data.mouse.model.MouseEvent

/**
 * The central class for receiving and interacting with the Mouse Events.
 * This is wrapper around [NativeMouseHandler] providing high-level access to the Mouse.
 *
 */
public class Mouse {

    /**
     * The backing [NativeMouseHandler].
     */
    public val handler: NativeMouseHandler = nativeMouseHandlerForPlatform()

    /**
     * Triggers [MouseEvent] on the host machine.
     */
    public fun send(event: MouseEvent): Unit = handler.sendEvent(event)
}
