package klib.data.keyboard

import klib.data.keyboard.model.Key
import klib.data.keyboard.model.KeyEvent
import klib.data.keyboard.model.KeySet
import klib.data.keyboard.model.KeyState
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.update
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.TimeSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * A typealias of lambda returned from [Keyboard.addShortcut] for better readability.
 */
public typealias Cancellable = () -> Unit

/**
 * Represents a keypress sequence with each element in ascending order of duration from the start time.
 */
public typealias KeyPressSequence = List<Pair<Duration, KeyEvent>>

/**
 * The central class for receiving and interacting with the Keyboard Events.
 * This is wrapper around [NativeKeyboardHandler] providing high-level access to the Keyboard.
 *
 * The handlers are always invoked in a new coroutine, to let the [handler] emit the events quickly without any delay.
 * The [Exception] should be handled with the help of [CoroutineExceptionHandler] in the [CoroutineContext] provided.
 *
 * @param context The [CoroutineContext] used for processing of data.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
public class Keyboard(context: CoroutineContext = Dispatchers.Default) {

    private val scope = CoroutineScope(context + SupervisorJob())
    private var job = AtomicReference<Job?>(null)

    private val keyDownHandlers = AtomicReference(mapOf<KeySet, suspend () -> Unit>())
    private val keyUpHandlers = AtomicReference(mapOf<KeySet, suspend () -> Unit>())

    /**
     * The backing [NativeKeyboardHandler].
     */
    public val handler: NativeKeyboardHandler = nativeKeyboardHandlerForPlatform()

    /**
     * Adds the [handler] to be invoked at [trigger] of the [keySet].
     *
     * @return Returns a [Cancellable], which when invoked the handler is removed.
     */
    public fun addShortcut(keySet: KeySet, trigger: KeyState = KeyState.KeyDown, handler: suspend () -> Unit): Cancellable {
        val handlers = if (trigger == KeyState.KeyDown) keyDownHandlers else keyUpHandlers

        handlers.update { it + (keySet to handler) }
        startIfNeeded()

        return {
            handlers.update { it - keySet }
            stopIfNeeded()
        }
    }

    /**
     * Presses and releases the [keySet] on the host machine.
     */
    public fun send(keySet: KeySet) {
        if (keySet.keys.isEmpty()) return

        for (key in keySet.keys)
            handler.sendEvent(KeyEvent(key, KeyState.KeyDown))

        for (key in keySet.keys)
            handler.sendEvent(KeyEvent(key, KeyState.KeyUp))
    }

    /**
     * Writes the following [string] on the host machine.
     */
    public fun write(string: String) {
        if (string.isEmpty()) return

        val capsState = handler.isCapsLockOn()
        for (char in string) {
            val (key, shift) = Key.fromChar(char)

            // Simplification of: char.toLowerCase() !in 'a'..'z' && shift || char.toLowerCase() in 'a'..'z' && shift != capsState
            if ((char.lowercaseChar() in 'a'..'z' && capsState) != shift) {
                handler.sendEvent(KeyEvent(Key.LeftShift, KeyState.KeyDown))
                handler.sendEvent(KeyEvent(key, KeyState.KeyDown))
                handler.sendEvent(KeyEvent(key, KeyState.KeyUp))
                handler.sendEvent(KeyEvent(Key.LeftShift, KeyState.KeyUp))
            }
            else {
                handler.sendEvent(KeyEvent(key, KeyState.KeyDown))
                handler.sendEvent(KeyEvent(key, KeyState.KeyUp))
            }
        }
    }

    /**
     * Suspends till the [keySet] are pressed.
     */
    public suspend fun awaitTill(keySet: KeySet, trigger: KeyState = KeyState.KeyDown): Unit = suspendCancellableCoroutine { cont ->
        val handlers = if (trigger == KeyState.KeyDown) keyDownHandlers else keyUpHandlers

        handlers.update {
            it + (keySet to {
                handlers.update { it - keySet }
                stopIfNeeded()
                cont.resume(Unit)
            })
        }
        startIfNeeded()

        cont.invokeOnCancellation {
            handlers.update { it - keySet }
            stopIfNeeded()
        }
    }

    /**
     * Records and returns a [KeyPressSequence] of all the keypress till a [keySet] is/are pressed.
     */
    public suspend fun recordKeyPressesTill(keySet: KeySet, trigger: KeyState = KeyState.KeyDown): KeyPressSequence = suspendCancellableCoroutine { cont ->
        val mark = TimeSource.Monotonic.markNow()
        val recording = AtomicBoolean(true)

        val handlers = if (trigger == KeyState.KeyDown) keyDownHandlers else keyUpHandlers

        handlers.update {
            it + (keySet to {
                recording.store(false)
                handlers.update { it - keySet }
                stopIfNeeded()
            })
        }
        startIfNeeded()

        val recJob = scope.launch {
            cont.resume(handler.events.map { mark.elapsedNow() to it }.takeWhile { recording.load() }.toList())
        }

        cont.invokeOnCancellation {
            recJob.cancel()
            handlers.update { it - keySet }
            stopIfNeeded()
        }
    }

    /**
     * Plays the given [orderedPresses] with a speed of [speedFactor].
     */
    public suspend fun play(orderedPresses: KeyPressSequence, speedFactor: Double = 1.0) {
        val mark = TimeSource.Monotonic.markNow()

        for ((duration, event) in orderedPresses) {
            delay((duration - mark.elapsedNow()) / speedFactor)
            handler.sendEvent(event)
        }
    }

    /**
     * Disposes this [Keyboard] instance.
     */
    public fun dispose() {
        scope.cancel(null)
        job.store(null)
    }

    private fun startIfNeeded() {
        val jobCopy = job.load()
        if (jobCopy != null && jobCopy.isActive) return

        job.store(
            scope.launch {
                val pressedKeys = mutableSetOf<Key>()

                handler.events.collect {
                    if (it.state == KeyState.KeyDown) {
                        pressedKeys.add(it.key)
                        handleKeyDown(pressedKeys)
                    }
                    else {
                        handleKeyUp(pressedKeys)
                        pressedKeys.remove(it.key)
                    }
                }
            },
        )
    }

    private fun stopIfNeeded() {
        if (keyDownHandlers.load().isNotEmpty()) return
        if (keyUpHandlers.load().isNotEmpty()) return

        val activeJob = job.load() ?: return
        activeJob.cancel()
        job.store(null)
    }

    private fun handleKeyDown(pressedKeys: Set<Key>) {
        for ((keySet, handler) in keyDownHandlers.load())
            if (pressedKeys.containsAll(keySet.keys)) {
                scope.launch { handler() }
                break
            }
    }

    private fun handleKeyUp(pressedKeys: Set<Key>) {
        for ((keySet, handler) in keyUpHandlers.load())
            if (pressedKeys.containsAll(keySet.keys)) {
                scope.launch { handler() }
                break
            }
    }
}
