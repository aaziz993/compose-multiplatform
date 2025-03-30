package gradle.plugins.kotlin.apollo

import gradle.accessors.catalog.libs

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.apollo.model.ApolloSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApolloPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.apollo?.takeIf{ pluginManager.hasPlugin("apollo") }?.let { apollo ->
                    plugins.apply(project.settings.libs.plugin("apollo3").id)

                    apollo.applyTo()
                }
        }
    }
}
