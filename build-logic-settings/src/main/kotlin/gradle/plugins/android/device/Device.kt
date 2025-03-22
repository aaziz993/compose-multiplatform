package gradle.plugins.android.device

import gradle.api.ProjectNamed
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Top-level interface for all devices to run applications by tasks in the Android Gradle Plugin.
 */
@Suppress("UnstableApiUsage")
internal interface Device<T : com.android.build.api.dsl.Device> : ProjectNamed<T>

@Serializable
@Suppress("UnstableApiUsage")
internal data class DeviceImpl(
    override val name: String? = null,,
) : Device<com.android.build.api.dsl.Device> {

    context(Project)
    override fun applyTo(recipient: com.android.build.api.dsl.Device) {
    }
}
