package klib.data.net

import java.awt.Desktop

public fun GlobalDeepLinkController.handle(args: Array<String>) {
    if (System.getProperty("os.name").indexOf("Mac") > -1)
        Desktop.getDesktop().setOpenURIHandler { uri ->
            handle(uri.uri.toString())
        }
    else args.getOrNull(0)?.let(::handle)
}
