package plugins.apple.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.plugins.apple.AppleProjectExtension
import gradle.plugins.apple.target.IosAppTarget
import gradle.plugins.apple.target.IosFrameworkTarget
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    override val iosApp: IosAppTarget? = null,
    override val iosFramework: IosFrameworkTarget? = null,
) : AppleProjectExtension {

    context(project: Project)
    override fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("apple").id) {
            super.applyTo()
        }
}
