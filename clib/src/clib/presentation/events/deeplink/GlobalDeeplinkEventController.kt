package clib.presentation.events.deeplink

import clib.presentation.events.EventController
import io.ktor.http.Url
import klib.data.net.toUrl

public object GlobalDeeplinkEventController : EventController<Url>() {

    /**
     * Handle a deep link.
     * @param url The deep link to handle.
     */
    public fun sendEvent(url: String): Unit = sendEvent(url.toUrl())
}
