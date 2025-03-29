package gradle.plugins.android.device

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Top-level interface for all devices to run applications by tasks in the Android Gradle Plugin.
 */
@Suppress("UnstableApiUsage")
internal interface Device<T : com.android.build.api.dsl.Device> : ProjectNamed<T>

@Suppress("UnstableApiUsage")
@KeepGeneratedSerializer
@Serializable(with = DeviceImlObjectTransformingSerializer::class)
internal data class DeviceImpl(
    override val name: String? = null,
) : Device<com.android.build.api.dsl.Device> {

    context(Project)
    override fun applyTo(receiver: com.android.build.api.dsl.Device) {
    }
}

private object DeviceImlObjectTransformingSerializer
    : NamedObjectTransformingSerializer<DeviceImpl>(DeviceImpl.generatedSerializer())
