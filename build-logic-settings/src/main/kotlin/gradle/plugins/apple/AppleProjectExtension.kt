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

    val sourceSets: LinkedHashSet<@Serializable(with = AppleSourceSetTransformingSerializer::class) AppleSourceSet>?

    val targets: LinkedHashSet<@Serializable(with = AppleTargetTransformingSerializer::class) AppleTarget<*>>?

    val teamID: String?
    val iosApp: IosAppTarget?
    val iosFramework: IosFrameworkTarget?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("apple").id) {
            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.apple.sourceSets)
            }

            targets?.forEach { target ->
                (target as AppleTarget<org.jetbrains.gradle.apple.targets.AppleTarget>?).applyTo(project.apple.targets)
            }

            project.apple.teamID = teamID ?: project.moduleName

            iosApp?.let { iosApp ->
                iosApp.name?.takeIf(String::isNotEmpty)?.also { name ->
                    project.apple.iosApp(name) { iosApp.applyTo(this) }
                } ?: project.apple.iosApp { iosApp.applyTo(this) }
            }

            iosFramework?.let { iosFramework ->
                iosFramework.name?.takeIf(String::isNotEmpty)?.also { name ->
                    project.apple.iosFramework(name) { iosFramework.applyTo(this) }
                } ?: project.apple.iosFramework { iosFramework.applyTo(this) }
            }
        }
}
