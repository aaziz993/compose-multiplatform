package plugin.project.apple.model

import gradle.nativeModuleName
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
        extension.teamID = teamID ?: nativeModuleName

        iosApps?.forEach { iosApp ->
            iosApp.name.takeIf(String::isNotEmpty)?.also { name ->
                extension.iosApp(name, iosApp::applyTo)
            } ?: extension.iosApp(iosApp::applyTo)
        }

        iosFrameworks?.forEach { iosFramework ->
            iosFramework.name.takeIf(String::isNotEmpty)?.also { name ->
                extension.iosFramework(name, iosFramework::applyTo)
            } ?: extension.iosFramework(iosFramework::applyTo)
        }
    }
}
