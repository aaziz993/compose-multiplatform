package gradle.plugins.apple.target

import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import gradle.plugins.apple.AppleBuildSettings
import gradle.plugins.apple.BuildConfiguration
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = AppleTargetSerializer::class)
internal interface AppleTarget<T : org.jetbrains.gradle.apple.targets.AppleTarget> : ProjectNamed<T> {

    val bridgingHeader: String?

    val buildConfigurations: Set<BuildConfiguration>?

    val buildSettings: Map<AppleBuildSettings, String>?

    val embedFrameworks: Boolean?

    val ipad: Boolean?

    val iphone: Boolean?

    val productInfo: SerializableAnyMap?

    val setProductInfo: SerializableAnyMap?

    val productModuleName: String?

    val productName: String?

    context(project: Project)
    override fun applyTo(receiver: T) {
        receiver::bridgingHeader trySet bridgingHeader

        buildConfigurations?.forEach { buildConfigurations ->
            buildConfigurations.applyTo(receiver.buildConfigurations)
        }

        buildSettings?.forEach { buildSettings ->
            buildSettings.key.applyTo(receiver.buildSettings, buildSettings.value)
        }

        receiver::embedFrameworks trySet embedFrameworks
        receiver::ipad trySet ipad
        receiver::iphone trySet iphone
        productInfo?.let(receiver.productInfo::putAll)
        setProductInfo?.act(receiver.productInfo::clear)?.let(receiver.productInfo::putAll)
        receiver::productModuleName trySet productModuleName
        receiver::productName trySet productName
    }
}

private object AppleTargetSerializer : JsonPolymorphicSerializer<AppleTarget<*>>(
    AppleTarget::class,
)

internal object AppleTargetTransformingSerializer : KeyTransformingSerializer<AppleTarget<*>>(
    AppleTargetSerializer,
    "type",
)
