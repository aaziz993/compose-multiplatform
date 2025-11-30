import SwiftUI
import clib

@main
struct IOSApp: App {

    // Shared KMM deep link handler
    let deepLinkHandler = DeepLinkStateIos()

    // Bridge to AppDelegate for Universal Links
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                // Handle custom URL schemes
                .onOpenURL { url in
                    deepLinkHandler.onDeepLinkReceived(url: url.absoluteString)
                }
        }
    }
}

// MARK: - AppDelegate for Universal Links
class AppDelegate: NSObject, UIApplicationDelegate {

    let deepLinkHandler = DeepLinkStateIos()

    // Handle URL schemes.
    func application(_ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
        deepLinkHandler.onDeepLinkReceived(url: url.absoluteString)
        return true
    }

    // Handle Universal Links (web links).
    func application(_ application: UIApplication,
        continue userActivity: NSUserActivity,
        restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        deepLinkHandler.onDeepLinkReceived(userActivity: userActivity)
        return true
    }
}
