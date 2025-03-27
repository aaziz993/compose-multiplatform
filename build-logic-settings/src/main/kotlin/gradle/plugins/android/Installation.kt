package gradle.plugins.android

import com.android.build.api.dsl.Installation
import gradle.api.tryAddAll
import gradle.api.trySet

/**
 * Local installation options for the adb tool.
 */
internal interface Installation<T : Installation> {

    /** The time out used for all adb operations. */
    val timeOutInMs: Int?

    /** The list of FULL_APK installation options. */
    val installOptions: List<String>?
    val setInstallOptions: List<String>?

    fun applyTo(receiver: T) {
        receiver::timeOutInMs trySet timeOutInMs
        receiver.installOptions tryAddAll installOptions
        receiver.installOptions trySet setInstallOptions
    }
}
