package klib.data.net

import klib.data.net.DeepLinkState.handleDeepLink
import platform.Foundation.NSUserActivity
import platform.Foundation.NSUserActivityTypeBrowsingWeb
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "DeepLinkState")
public class DeepLinkStateApple {

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(url: String): Unit = handleDeepLink(url)

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(userActivity: NSUserActivity) {
        userActivity.getUrlString()?.let(::handleDeepLink)
    }

    private fun NSUserActivity.getUrlString() =
        if (activityType == NSUserActivityTypeBrowsingWeb) webpageURL?.absoluteString else null
}
