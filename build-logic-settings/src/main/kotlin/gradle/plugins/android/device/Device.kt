package gradle.plugins.android.device

import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Top-level interface for all devices to run applications by tasks in the Android Gradle Plugin.
 */
@Suppress("UnstableApiUsage")
internal interface Device<T : com.android.build.api.dsl.Device> : ProjectNamed<T>

internal abstract class DeviceKeyTransformingSerializer<T : Device<*>>(
    tSerializer: KSerializer<T>
) : NamedKeyTransformingSerializer<T>(tSerializer)

@Suppress("UnstableApiUsage")
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = DeviceImlKeyTransformingSerializer::class)
internal data class DeviceImpl(
    override val name: String? = null,
) : Device<com.android.build.api.dsl.Device> {

    context(Project)
    override fun applyTo(receiver: com.android.build.api.dsl.Device) {
    }
}

private object DeviceImlKeyTransformingSerializer
    : DeviceKeyTransformingSerializer<DeviceImpl>(DeviceImpl.generatedSerializer())
