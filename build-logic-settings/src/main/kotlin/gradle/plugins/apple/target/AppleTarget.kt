package gradle.plugins.apple.target

import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.maybeNamed
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

    context(Project)
    override fun applyTo(recipient: T) {
        recipient::bridgingHeader trySet bridgingHeader

        buildConfigurations?.forEach { buildConfigurations ->
            buildConfigurations.applyTo(recipient.buildConfigurations)
        }

        buildSettings?.forEach { buildSettings ->
            buildSettings.key.applyTo(recipient.buildSettings, buildSettings.value)
        }

        recipient::embedFrameworks trySet embedFrameworks
        recipient::ipad trySet ipad
        recipient::iphone trySet iphone
        productInfo?.let(recipient.productInfo::putAll)
        setProductInfo?.act(recipient.productInfo::clear)?.let(recipient.productInfo::putAll)
        recipient::productModuleName trySet productModuleName
        recipient::productName trySet productName
    }
}

private object AppleTargetSerializer : JsonPolymorphicSerializer<AppleTarget<*>>(
    AppleTarget::class,
)

internal object AppleTargetTransformingSerializer : KeyTransformingSerializer<AppleTarget<*>>(
    AppleTargetSerializer,
    "type",
)
