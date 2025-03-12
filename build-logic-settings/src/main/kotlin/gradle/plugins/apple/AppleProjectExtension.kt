package gradle.plugins.apple

import gradle.accessors.apple
import gradle.accessors.moduleName
import org.gradle.api.Project

internal interface AppleProjectExtension {

    val teamID: String?
    val iosApp: IosAppTarget?
    val iosFramework: IosFrameworkTarget?

    context(Project)
    fun applyTo() {
        apple.teamID = teamID ?: moduleName

        iosApp?.let { iosApp ->
            iosApp.name.takeIf(String::isNotEmpty)?.also { name ->
                apple.iosApp(name, iosApp::applyTo)
            } ?: apple.iosApp(iosApp::applyTo)
        }

        iosFramework?.let { iosFramework ->
            iosFramework.name.takeIf(String::isNotEmpty)?.also { name ->
                apple.iosFramework(name, iosFramework::applyTo)
            } ?: apple.iosFramework(iosFramework::applyTo)
        }
    }
}
