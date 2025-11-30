package clib.presentation.navigation.deeplink

import android.app.Activity

public fun Activity.handleDeepLink() {
    intent?.data?.let { uri ->
        ExternalUriHandler.onNewUri(uri.toString())
    }
}
