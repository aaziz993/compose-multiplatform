package gradle.plugins.apple.target

import gradle.api.ProjectNamed
import gradle.api.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import klib.data.type.collection.tryPutAll
import klib.data.type.collection.trySet
import gradle.plugins.apple.AppleBuildSettings
import gradle.plugins.apple.BuildConfiguration
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.ReflectionJsonObjectTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = ReflectionAppleTargetObjectTransformingPolymorphicSerializer::class)
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

private class ReflectionAppleTargetObjectTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionJsonObjectTransformingPolymorphicSerializer<AppleTarget<*>>(
    AppleTarget::class,
)
