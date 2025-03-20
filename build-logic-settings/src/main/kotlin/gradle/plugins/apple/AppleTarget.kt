package gradle.plugins.apple

import gradle.api.maybeNamed
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import org.jetbrains.gradle.apple.targets.AppleTarget

internal interface AppleTarget<T : AppleTarget> {

    val bridgingHeader: String?

    val buildConfigurations: List<BuildConfiguration>?

    val buildSettings: Map<AppleBuildSettings, String>?

    val embedFrameworks: Boolean?

    val ipad: Boolean?

    val iphone: Boolean?

    val name: String?

    val productInfo: SerializableAnyMap?
    val setProductInfo: SerializableAnyMap?

    val productModuleName: String?

    val productName: String?

    fun applyTo(recipient: T) {
        recipient::bridgingHeader trySet bridgingHeader

        buildConfigurations?.forEach { buildConfigurations ->
            buildConfigurations.name.takeIf(String::isNotEmpty)?.also { name ->
                recipient.buildConfigurations.maybeNamed(name, buildConfigurations::applyTo)
            } ?: recipient.buildConfigurations.all(buildConfigurations::applyTo)
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

