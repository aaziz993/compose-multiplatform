package clib.presentation.events.deeplink

import java.awt.Desktop

public fun GlobalDeeplinkEventController.handleEvents(args: Array<String>) {
    if (System.getProperty("os.name").indexOf("Mac") > -1)
        Desktop.getDesktop().setOpenURIHandler { uri ->
            sendEvent(uri.uri.toString())
        }
    else args.getOrNull(0)?.let(::sendEvent)
}
