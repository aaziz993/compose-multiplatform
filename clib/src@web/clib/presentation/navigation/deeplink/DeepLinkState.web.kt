package clib.presentation.navigation.deeplink

import clib.presentation.navigation.deeplink.DeepLinkState.handleDeepLink
import web.events.addEventListener
import web.events.removeEventListener
import web.history.HASH_CHANGE
import web.history.HashChangeEvent
import web.history.POP_STATE
import web.history.PopStateEvent
import web.window.window

public fun handleDeepLink() {
    // Handle initial URL.
    handleDeepLink(window.location.href)

    // Handle future changes (e.g., user navigates, app updates hash, etc.).
    window.addEventListener(
        PopStateEvent.POP_STATE,
        {
            handleDeepLink(window.location.href)
        },
    )

    // Optional: if your app uses hash-based routing.
    window.addEventListener(
        HashChangeEvent.HASH_CHANGE,
        {
            handleDeepLink(window.location.href)
        },
    )
}

public fun disposeDeepLink() {
    window.removeEventListener(
        PopStateEvent.POP_STATE,
        {},
    )

    window.removeEventListener(
        HashChangeEvent.HASH_CHANGE,
        {},
    )
}
