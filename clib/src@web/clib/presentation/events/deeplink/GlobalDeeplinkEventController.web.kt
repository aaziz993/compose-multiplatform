package clib.presentation.events.deeplink

import web.events.addHandler
import web.window.hashChangeEvent
import web.window.popStateEvent
import web.window.window

public fun addHandleDeeplinkEvents(
    handle: (String) -> Unit = GlobalDeeplinkEventController::sendEvent
): Pair<() -> Unit, () -> Unit> =
    // Handle future changes (e.g., user navigates, app updates hash, etc.).
    window.popStateEvent.addHandler { handle(window.location.href) } to
        // Optional: if your app uses hash-based routing.
        window.hashChangeEvent.addHandler { handle(window.location.href) }
