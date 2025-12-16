package klib.data.net

import klib.data.net.GlobalDeepLinkController.handle
import platform.Foundation.NSUserActivity
import platform.Foundation.NSUserActivityTypeBrowsingWeb
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "GlobalDeepLinkController")
public class GlobalDeepLinkControllerApple {

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(url: String): Unit = handle(url)

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(userActivity: NSUserActivity) {
        userActivity.getUrlString()?.let(::handle)
    }

    private fun NSUserActivity.getUrlString() =
        if (activityType == NSUserActivityTypeBrowsingWeb) webpageURL?.absoluteString else null
}
