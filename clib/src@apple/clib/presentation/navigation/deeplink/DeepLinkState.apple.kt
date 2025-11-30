package clib.presentation.navigation.deeplink

import clib.presentation.navigation.deeplink.DeepLinkState.handleDeepLink
import platform.Foundation.NSUserActivity
import platform.Foundation.NSUserActivityTypeBrowsingWeb
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "DeepLinkStateIos")
public class DeepLinkStateIos {

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(url: String): Unit = handleDeepLink(url)

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(userActivity: NSUserActivity) {
        userActivity.getUrlString()?.let { url -> handleDeepLink(url) }
    }

    private fun NSUserActivity.getUrlString() =
        if (activityType == NSUserActivityTypeBrowsingWeb) webpageURL?.absoluteString else null
}
