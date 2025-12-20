package clib.presentation.events.deeplink

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.util.Consumer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

context(activity: ComponentActivity)
public fun GlobalDeeplinkEventController.handleEvents() {
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

private fun GlobalDeeplinkEventController.fireDeepLink(intent: Intent) {
    intent.dataString?.let(::sendEvent)
}
