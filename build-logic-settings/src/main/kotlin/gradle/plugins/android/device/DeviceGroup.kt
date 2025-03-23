package gradle.plugins.android.device

import com.android.build.api.dsl.DeviceGroup
import gradle.api.ProjectNamed
import gradle.api.applyTo
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * A group of devices to be run with tests using the Unified Test Platform.
 */
@Serializable
internal data class DeviceGroup(
    override val name: String? = null,
    val targetDevices: Set<DeviceImpl>? = null,
) : ProjectNamed<DeviceGroup> {

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(recipient: DeviceGroup) {
        targetDevices?.forEach { targetDevice ->
            targetDevice.applyTo(recipient.targetDevices) { _, _ -> }
        }
    }
}
