package klib.data.net

import io.ktor.http.Url
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

public object GlobalDeepLinkController {

    /** Coroutine scope for executing actions on the main thread. */
    private val mainScope = MainScope()

    private val _events = MutableSharedFlow<Url>(replay = 1)
    public val events: Flow<Url> = _events.asSharedFlow()

    /**
     * Handle a deep link.
     * @param url The deep link to handle.
     */
    public fun handle(url: String) {
        mainScope.launch {
            _events.emit(url.toUrl())
        }
    }
}
