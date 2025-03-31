package gradle.plugins.android

import com.android.build.gradle.internal.dsl.AdbOptions
import gradle.collection.tryAddAll
import kotlinx.serialization.Serializable

/**
 * Options for adb.
 */
@Serializable
internal data class AdbOptions(
    /** The time out used for all adb operations. */
    val timeOutInMs: Int? = null,
    /** The list of APK installation options. */
    val installOptions: Set<String>? = null,
    val setInstallOptions: Set<String>? = null
) {

    fun applyTo(receiver: AdbOptions) {
        timeOutInMs?.let(receiver::timeOutInMs)
        receiver.installOptions tryAddAll installOptions
        setInstallOptions?.toTypedArray()?.let(receiver::setInstallOptions)
    }
}
