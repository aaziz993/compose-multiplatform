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
            projectProperties.plugins.apollo
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { apollo ->
                    plugins.apply(settings.libs.plugins.plugin("apollo3").id)

                    apollo.applyTo()
                }
        }
    }
}
