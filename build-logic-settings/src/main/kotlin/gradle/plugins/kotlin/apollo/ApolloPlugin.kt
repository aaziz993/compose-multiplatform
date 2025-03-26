package gradle.plugins.kotlin.apollo

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import gradle.plugins.kotlin.apollo.model.ApolloSettings

internal class ApolloPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.apollo
                .takeIf(ApolloSettings::enabled)?.let { apollo ->
                    plugins.apply(project.settings.libs.plugins.plugin("apollo3").id)

                    apollo.applyTo()
                }
        }
    }
}
