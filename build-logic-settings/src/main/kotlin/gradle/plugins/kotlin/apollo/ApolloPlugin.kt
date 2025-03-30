package gradle.plugins.kotlin.apollo

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ApolloPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply apollo properties.
            projectProperties.apollo?.applyTo()
        }
    }
}
