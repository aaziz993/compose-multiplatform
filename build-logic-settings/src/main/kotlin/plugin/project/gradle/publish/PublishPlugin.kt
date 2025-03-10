package plugin.project.gradle.publish

import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

internal class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.publishing
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { publishing ->
                    plugins.apply(MavenPublishPlugin::class.java)

                    publishing.applyTo()
                }
        }
    }
}
