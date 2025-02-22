package plugin.project.kotlin.apollo

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApolloPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (settings.projectProperties.plugins.apollo.enabled ||settings.projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.apollo3.get().pluginId)

            configureApolloExtension()
        }
    }
}
