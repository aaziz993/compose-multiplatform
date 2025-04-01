package gradle.plugins.android

import com.android.build.api.dsl.Installation
import klib.data.type.collection.tryAddAll
import klib.data.type.collection.trySet
import klib.data.type.reflection.trySet

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
