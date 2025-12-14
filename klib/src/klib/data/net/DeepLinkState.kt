package klib.data.net

import io.ktor.http.Url
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

public object DeepLinkState {

    /** Coroutine scope for executing actions on the main thread. */
    private val mainScope = MainScope()

    public var deepLinkFlow: MutableStateFlow<Url?> = MutableStateFlow(null)
        private set

    /**
     * Handle a deep link.
     * @param url The deep link to handle.
     */
    public fun handleDeepLink(url: String) {
        mainScope.launch {
            deepLinkFlow.emit(url.toUrl())
        }
    }

    public fun consumeDeepLink(): Url? = deepLinkFlow.getAndUpdate { null }
}
