package clib.presentation.navigation.deeplink

import java.awt.Desktop

public fun handleDeepLink(args: Array<String>) {
    if (System.getProperty("os.name").indexOf("Mac") > -1)
        Desktop.getDesktop().setOpenURIHandler { uri ->
            ExternalUriHandler.onNewUri(uri.uri.toString())
        }
    else args.getOrNull(0)?.let(ExternalUriHandler::onNewUri)
}
