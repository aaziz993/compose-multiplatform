package klib.data.net

import java.awt.Desktop
import klib.data.net.GlobalDeepLinkController.handle

public fun handleDeepLink(args: Array<String>) {
    if (System.getProperty("os.name").indexOf("Mac") > -1)
        Desktop.getDesktop().setOpenURIHandler { uri ->
            handle(uri.uri.toString())
        }
    else args.getOrNull(0)?.let(::handle)
}
