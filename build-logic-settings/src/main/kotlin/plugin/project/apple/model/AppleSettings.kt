package plugin.project.apple.model

import gradle.apple
import gradle.id
import gradle.libs
import gradle.model.kotlin.kmp.apple.AppleProjectExtension
import gradle.model.kotlin.kmp.apple.IosAppTarget
import gradle.model.kotlin.kmp.apple.IosFrameworkTarget
import gradle.plugin
import gradle.plugins
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    val iosApps: List<IosAppTarget>? = null,
    val iosFrameworks: List<IosFrameworkTarget>? = null,
) : AppleProjectExtension {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("apple").id) {
            super.applyTo()

            iosApps?.forEach { iosApp ->
                iosApp.name.takeIf(String::isNotEmpty)?.also { name ->
                    apple.iosApp(name, iosApp::applyTo)
                } ?: apple.iosApp(iosApp::applyTo)
            }

            iosFrameworks?.forEach { iosFramework ->
                iosFramework.name.takeIf(String::isNotEmpty)?.also { name ->
                    apple.iosFramework(name, iosFramework::applyTo)
                } ?: apple.iosFramework(iosFramework::applyTo)
            }
        }
}
