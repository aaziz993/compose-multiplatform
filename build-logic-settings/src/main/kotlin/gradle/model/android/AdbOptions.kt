package gradle.model.android

import com.android.build.gradle.internal.dsl.AdbOptions
import kotlinx.serialization.Serializable

/**
 * Options for adb.
 */
@Serializable
internal data class AdbOptions(
    /** The time out used for all adb operations. */
    val timeOutInMs: Int? = null,
    /** The list of APK installation options. */
    val installOptions: List<String>? = null
){
    fun applyTo(options: AdbOptions) {
        timeOutInMs?.let(options::timeOutInMs)
        installOptions?.let(options.installOptions::addAll)
        options.dslServices
    }
}
