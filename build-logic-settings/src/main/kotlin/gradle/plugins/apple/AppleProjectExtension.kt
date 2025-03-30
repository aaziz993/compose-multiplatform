package gradle.plugins.apple

import gradle.accessors.apple
import gradle.accessors.moduleName
import gradle.api.applyTo
import gradle.plugins.apple.target.AppleTarget
import gradle.plugins.apple.target.IosAppTarget
import gradle.plugins.apple.target.IosFrameworkTarget
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleProjectExtension(
    val sourceSets: LinkedHashSet<AppleSourceSet>? = null,
    val targets: LinkedHashSet<AppleTarget<out org.jetbrains.gradle.apple.targets.AppleTarget>>? = null,
    val teamID: String? = null,
    val iosApp: IosAppTarget? = null,
    val iosFramework: IosFrameworkTarget? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.gradle.apple.applePlugin") {
            sourceSets?.forEach { sourceSet ->
                sourceSet.applyTo(project.apple.sourceSets)
            }

            targets?.forEach { target ->
                (target as AppleTarget<org.jetbrains.gradle.apple.targets.AppleTarget>).applyTo(project.apple.targets)
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
