import SwiftUI
import clib

// AppDelegate.
class AppDelegate: NSObject, UIApplicationDelegate {

    // Shared KMM deep link controller.
    let deepLinkController = GlobalDeeplinkEventControllerApple()

    // Handle URL schemes.
    func application(_ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
        deepLinkController.onDeepLinkReceived(url: url.absoluteString)
        return true
    }

    // Handle Universal Links (web links).
    func application(_ application: UIApplication,
        continue userActivity: NSUserActivity,
        restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        deepLinkController.onDeepLinkReceived(userActivity: userActivity)
        return true
    }
}

@main
struct IOSApp: App {

    // Shared KMM deep link controller.
    let deepLinkController = GlobalDeeplinkEventControllerApple()

    // Bridge to AppDelegate.
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                // Handle custom URL schemes.
                .onOpenURL { url in
                    deepLinkController.onDeepLinkReceived(url: url.absoluteString)
                    // Handle supabase deep link url.
                    // If using kmauth-supabase.
                    KMAuthSupabase.deepLinkHandler().handleDeepLinks(url: url)
                }
        }
    }
}
