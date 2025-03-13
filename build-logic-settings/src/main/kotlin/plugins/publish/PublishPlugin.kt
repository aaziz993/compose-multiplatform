package plugins.publish

import gradle.accessors.projectProperties
import gradle.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.plugins.signing.SigningPlugin

internal class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.publishing
                .takeIf {
                    it.enabled && projectProperties.kotlin.targets.isNotEmpty() && projectProperties.type == ProjectType.LIB
                }?.let { publishing ->
                    plugins.apply(MavenPublishPlugin::class.java)

                    publishing.applyTo()
                }
        }
    }
}
