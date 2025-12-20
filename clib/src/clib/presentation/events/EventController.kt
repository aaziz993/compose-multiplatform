package clib.presentation.events

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public abstract class EventController<T> {

    /** Coroutine scope for executing actions on the main thread. */
    private val mainScope = MainScope()

    private val _events = Channel<T>()

    public val events: Flow<T> = _events.receiveAsFlow()

    public fun sendEvent(event: T) {
        mainScope.launch {
            _events.send(event)
        }
    }
}
