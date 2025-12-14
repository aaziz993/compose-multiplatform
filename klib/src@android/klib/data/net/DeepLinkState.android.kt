package klib.data.net

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.util.Consumer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import klib.data.net.DeepLinkState.handleDeepLink

public fun ComponentActivity.handleDeepLink() {
    intent.fireDeepLink()

    val listener = Consumer<Intent> { intent ->
        intent.fireDeepLink()
    }

    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                this@handleDeepLink.addOnNewIntentListener(listener)
                super.onCreate(owner)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                this@handleDeepLink.removeOnNewIntentListener(listener)
                super.onDestroy(owner)
            }
        },
    )
}

private fun Intent.fireDeepLink() {
    dataString?.let(::handleDeepLink)
}
