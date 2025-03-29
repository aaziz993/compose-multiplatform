package gradle.plugins.apple.target

import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.tryPutAll
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.apple.AppleBuildSettings
import gradle.plugins.apple.BuildConfiguration
import gradle.serialization.serializer.JsonKeyValueTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = AppleTargetKeyTransformingContentPolymorphicSerializer::class)
internal interface AppleTarget<T : org.jetbrains.gradle.apple.targets.AppleTarget> : ProjectNamed<T> {

    val bridgingHeader: String?

    val buildConfigurations: LinkedHashSet<BuildConfiguration>?

    val buildSettings: Map<AppleBuildSettings, String>?

    val embedFrameworks: Boolean?

    val ipad: Boolean?

    val iphone: Boolean?

    val productInfo: SerializableAnyMap?

    val setProductInfo: SerializableAnyMap?

    val productModuleName: String?

    val productName: String?

    context(Project)
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
        receiver.productInfo tryPutAll productInfo
        receiver.productInfo trySet setProductInfo
        receiver::productModuleName trySet productModuleName
        receiver::productName trySet productName
    }
}

private object AppleTargetKeyTransformingContentPolymorphicSerializer : JsonKeyValueTransformingContentPolymorphicSerializer<AppleTarget<*>>(
    AppleTarget::class,
)
