package plugins.gradle.publish

import gradle.plugins.project.ProjectType
import gradle.accessors.projectProperties
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
                    plugins.apply(SigningPlugin::class.java)
                    "signing"
                    publishing.applyTo()
                }
        }
    }
}
