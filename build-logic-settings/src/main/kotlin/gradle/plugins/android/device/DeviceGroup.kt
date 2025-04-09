package gradle.plugins.android.device

import com.android.build.api.dsl.Device
import gradle.api.NamedMapTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.applyTo
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * A group of devices to be run with tests using the Unified Test Platform.
 */
@KeepGeneratedSerializer
@Serializable(with = DeviceGroupMapTransformingSerializer::class)
internal data class DeviceGroup(
    override val name: String? = null,
    val targetDevices: LinkedHashSet<DeviceImpl>? = null,
) : ProjectNamed<com.android.build.api.dsl.DeviceGroup> {

    context(Project)
    @Suppress("UnstableApiUsage")
    override fun applyTo(receiver: com.android.build.api.dsl.DeviceGroup) {
        targetDevices?.forEach { targetDevice ->
            targetDevice.applyTo(receiver.targetDevices, Device::getName) { _, _ -> }
        }
    }
}

private object DeviceGroupMapTransformingSerializer
    : NamedMapTransformingSerializer<DeviceGroup>(DeviceGroup.generatedSerializer())
