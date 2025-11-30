package clib.presentation.navigation.deeplink

import java.awt.Desktop
import clib.presentation.navigation.deeplink.DeepLinkState.handleDeepLink

public fun handleDeepLink(args: Array<String>) {
    if (System.getProperty("os.name").indexOf("Mac") > -1)
        Desktop.getDesktop().setOpenURIHandler { uri ->
            handleDeepLink(uri.uri.toString())
        }
    else args.getOrNull(0)?.let(::handleDeepLink)
}
