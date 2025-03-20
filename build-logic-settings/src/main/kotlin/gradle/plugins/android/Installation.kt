package gradle.plugins.android

import com.android.build.api.dsl.Installation
import gradle.api.trySet
import gradle.collection.act

/**
 * Local installation options for the adb tool.
 */
internal interface Installation<in T : Installation> {

    /** The time out used for all adb operations. */
    var timeOutInMs: Int?

    /** The list of FULL_APK installation options. */
    val installOptions: List<String>?
    val setInstallOptions: List<String>?

    fun applyTo(recipient: T) {
        recipient::timeOutInMs trySet timeOutInMs
        installOptions?.let(recipient.installOptions::addAll)
        setInstallOptions?.act(recipient.installOptions::clear)?.let(recipient.installOptions::addAll)
    }
}
