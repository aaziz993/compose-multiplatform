package gradle.model.android

import com.android.build.api.dsl.Installation
import gradle.trySet

/**
 * Local installation options for the adb tool.
 */
internal interface Installation {

    /** The time out used for all adb operations. */
    var timeOutInMs: Int?

    /** The list of FULL_APK installation options. */
    val installOptions: List<String>?

    fun applyTo(installation: Installation) {
        installation::timeOutInMs trySet timeOutInMs
        installOptions?.let(installation.installOptions::addAll)
    }
}
