package gradle.plugins.githooks

import gradle.api.initialization.initializationProperties
import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

internal class GitHooksPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        with(target) {
            // Apply gitHooks properties.
            initializationProperties.gitHooks?.applyTo()
        }
    }
}
