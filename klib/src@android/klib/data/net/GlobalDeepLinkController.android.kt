package klib.data.net

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.util.Consumer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

context(activity: ComponentActivity)
public fun GlobalDeepLinkController.handle() {
    fireDeepLink(activity.intent)

    val listener = Consumer(::fireDeepLink)
    activity.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                activity.addOnNewIntentListener(listener)
                super.onCreate(owner)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                activity.removeOnNewIntentListener(listener)
                super.onDestroy(owner)
            }
        },
    )
}

private fun GlobalDeepLinkController.fireDeepLink(intent: Intent) {
    intent.dataString?.let(::handle)
}
