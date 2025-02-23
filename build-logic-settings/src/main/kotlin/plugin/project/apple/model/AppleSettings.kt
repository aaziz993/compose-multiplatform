package plugin.project.apple.model

import gradle.projectProperties
import gradle.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class AppleSettings(
    override val teamID: String? = null,
    val iosApps: List<IosAppTarget>? = null,
    val iosFrameworks: List<IosFrameworkTarget>? = null,
) : AppleProjectExtension {

    context(Project)
    fun applyTo(extension: org.jetbrains.gradle.apple.AppleProjectExtension) {
        extension::teamID trySet teamID

        if (projectProperties.application) {
            iosApps?.forEach { iosApp ->
                iosApp.name.takeIf(String::isNotEmpty)?.also { name ->
                    extension.iosApp(name, iosApp::applyTo)
                } ?: extension.iosApp(iosApp::applyTo)
            }
        }
        else {

            iosFrameworks?.forEach { iosFramework ->
                iosFramework.name.takeIf(String::isNotEmpty)?.also { name ->
                    extension.iosFramework(name, iosFramework::applyTo)
                } ?: extension.iosFramework(iosFramework::applyTo)
            }
        }
    }
}
