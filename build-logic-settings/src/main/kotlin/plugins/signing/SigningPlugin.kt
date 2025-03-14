package plugins.signing

import org.gradle.kotlin.dsl.register
import gradle.accessors.projectProperties
import gradle.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.Exec
import org.gradle.plugins.signing.SigningPlugin

internal class SigningPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.signing
                .takeIf {
                    it.enabled && projectProperties.kotlin.targets.isNotEmpty()
                }?.let { signing ->
                    plugins.apply(SigningPlugin::class.java)

                    signing.applyTo()

                    tasks.register<Exec>("distributeSigningKey") {
                        commandLine("gpg", "--keyserver", "keys.openpgp.org '--send-keys", "yourKey")
                        args("server", "key")
                    }
                }
        }
    }
}
