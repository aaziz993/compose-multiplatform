package plugin.project.apple.model

import gradle.apple
import gradle.id
import gradle.libs
import gradle.plugins.apple.AppleProjectExtension
import gradle.plugins.apple.IosAppTarget
import gradle.plugins.apple.IosFrameworkTarget
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    override val iosApp: IosAppTarget? = null,
    override val iosFramework: IosFrameworkTarget? = null,
) : AppleProjectExtension {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("apple").id) {
            super.applyTo()
        }
}
