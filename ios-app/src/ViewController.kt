import androidx.compose.ui.window.ComposeUIViewController

public fun ViewController(): UIViewController = ComposeUIViewController({
    // Since 1.7.0-beta01 you have to add CADisableMinimumFrameDurationOnPhone property in your info.plist
    //release notes: https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.7.0-beta01
    // The app will crash by default, if CADisableMinimumFrameDurationOnPhone is not set to true in Info.plist.
    // Use newly added ComposeUIViewControllerConfiguration.enforceStrictPlistSanityCheck to opt-out of this behavior
    enforceStrictPlistSanityCheck = false
}) {
    Screen()
}
