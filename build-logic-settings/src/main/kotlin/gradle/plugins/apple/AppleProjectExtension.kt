package gradle.plugins.apple

import gradle.accessors.apple
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.moduleName
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.applyTo
import gradle.plugins.apple.target.AppleTarget
import gradle.plugins.apple.target.AppleTargetTransformingSerializer
import gradle.plugins.apple.target.IosAppTarget
import gradle.plugins.apple.target.IosFrameworkTarget
import kotlinx.serialization.Serializable
import org.gradle.api.Project

internal interface AppleProjectExtension {

    val sourceSets: Set<AppleSourceSet>?

    val targets: Set<@Serializable(with = AppleTargetTransformingSerializer::class) AppleTarget>?

    val teamID: String?
    val iosApp: IosAppTarget?
    val iosFramework: IosFrameworkTarget?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("apple").id) {
            targets?.forEach { target ->
                target.applyTo(apple.targets)
            }

            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(apple.sourceSets)
            }

            apple.teamID = teamID ?: moduleName

            iosApp?.let { iosApp ->
                iosApp.name?.takeIf(String::isNotEmpty)?.also { name ->
                    apple.iosApp(name, iosApp::applyTo)
                } ?: apple.iosApp(iosApp::applyTo)
            }

            iosFramework?.let { iosFramework ->
                iosFramework.name?.takeIf(String::isNotEmpty)?.also { name ->
                    apple.iosFramework(name, iosFramework::applyTo)
                } ?: apple.iosFramework(iosFramework::applyTo)
            }
        }
}
