package plugin.project.kotlin.apollo

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApolloPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.plugins.apollo.enabled ||projectProperties.kotlin.hasTargets) {
                return@with
            }

            plugins.apply(settings.libs.plugins.plugin("apollo3").id)

            configureApolloExtension()
        }
    }
}
