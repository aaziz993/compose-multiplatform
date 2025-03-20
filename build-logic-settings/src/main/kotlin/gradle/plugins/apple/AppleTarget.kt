package gradle.plugins.apple

import gradle.api.maybeNamed
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import org.jetbrains.gradle.apple.targets.AppleTarget

internal interface AppleTarget {

    val bridgingHeader: String?

    val buildConfigurations: List<BuildConfiguration>?

    val buildSettings: Map<AppleBuildSettings, String>?

    val embedFrameworks: Boolean?

    val ipad: Boolean?

    val iphone: Boolean?

    val name: String?

    val productInfo: SerializableAnyMap?

    val productModuleName: String?

    val productName: String?

    fun applyTo(recipient: AppleTarget) {
        target::bridgingHeader trySet bridgingHeader

        buildConfigurations?.forEach { buildConfigurations ->
            buildConfigurations.name.takeIf(String::isNotEmpty)?.also { name ->
                target.buildConfigurations.maybeNamed(name, buildConfigurations::applyTo)
            } ?: target.buildConfigurations.all(buildConfigurations::applyTo)
        }

        buildSettings?.forEach { buildSettings ->
            buildSettings.key.applyTo(target.buildSettings, buildSettings.value)

        }

        target::embedFrameworks trySet embedFrameworks
        target::ipad trySet ipad
        target::iphone trySet iphone
        productInfo?.let(target.productInfo::putAll)
        target::productModuleName trySet productModuleName
        target::productName trySet productName
    }
}

