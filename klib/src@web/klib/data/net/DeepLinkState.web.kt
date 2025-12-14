package klib.data.net

import klib.data.net.DeepLinkState.handleDeepLink
import web.events.addEventListener
import web.events.removeEventListener
import web.history.HASH_CHANGE
import web.history.HashChangeEvent
import web.history.POP_STATE
import web.history.PopStateEvent
import web.window.window

public fun addDeepLinkHandler(
    handle: (String) -> Unit = ::handleDeepLink
): Pair<(PopStateEvent) -> Unit, (HashChangeEvent) -> Unit> {
    val popStateHandler: (PopStateEvent) -> Unit = { handle(window.location.href) }
    val hashChangeHandler: (HashChangeEvent) -> Unit = { handle(window.location.href) }

    // Handle future changes (e.g., user navigates, app updates hash, etc.).
    window.addEventListener(
        PopStateEvent.POP_STATE,
        popStateHandler,
    )

    // Optional: if your app uses hash-based routing.
    window.addEventListener(
        HashChangeEvent.HASH_CHANGE,
        hashChangeHandler,
    )

    return popStateHandler to hashChangeHandler
}

public fun removeDeepLinkHandler(
    popStateHandler: (PopStateEvent) -> Unit,
    hashChangeHandler: (HashChangeEvent) -> Unit,
) {
    window.removeEventListener(
        PopStateEvent.POP_STATE,
        popStateHandler,
    )

    window.removeEventListener(
        HashChangeEvent.HASH_CHANGE,
        hashChangeHandler,
    )
}
