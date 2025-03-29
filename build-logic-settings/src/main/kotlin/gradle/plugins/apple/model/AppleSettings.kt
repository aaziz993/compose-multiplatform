package gradle.plugins.apple.model

import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.plugins.apple.AppleProjectExtension
import gradle.plugins.apple.AppleSourceSet
import gradle.plugins.apple.target.AppleTarget
import gradle.plugins.apple.target.IosAppTarget
import gradle.plugins.apple.target.IosFrameworkTarget
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSettings(
    override val sourceSets: LinkedHashSet<AppleSourceSet>? = null,
    override val targets: LinkedHashSet<AppleTarget<out org.jetbrains.gradle.apple.targets.AppleTarget>>? = null,
    override val teamID: String? = null,
    override val iosApp: IosAppTarget? = null,
    override val iosFramework: IosFrameworkTarget? = null,
) : AppleProjectExtension {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugin("apple").id) {
            super.applyTo()
        }
}
