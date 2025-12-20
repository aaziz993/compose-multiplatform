package clib.presentation.events.deeplink

import kotlin.experimental.ExperimentalObjCName
import platform.Foundation.NSUserActivity
import platform.Foundation.NSUserActivityTypeBrowsingWeb

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "GlobalDeeplinkEventControllerApple")
public class GlobalDeeplinkEventControllerApple {

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(url: String): Unit = GlobalDeeplinkEventController.sendEvent(url)

    @ObjCName("onDeepLinkReceived")
    public fun onDeepLinkReceived(userActivity: NSUserActivity) {
        userActivity.getUrlString()?.let(GlobalDeeplinkEventController::sendEvent)
    }

    private fun NSUserActivity.getUrlString() =
        if (activityType == NSUserActivityTypeBrowsingWeb) webpageURL?.absoluteString else null
}
