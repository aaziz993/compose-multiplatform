package plugins.signing

import org.gradle.kotlin.dsl.register
import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.accessors.signing
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

                    registerSigningGPGKeyDistributeTask()
                }
        }
    }

    /** Distribute signing gpg key
     * There are 3 servers supported by Central servers: [ keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu ]
     */
    private fun Project.registerSigningGPGKeyDistributeTask() {
        projectProperties.plugins.signing.useInMemoryPgpKeys?.defaultSecretKey
            ?.resolveValue()
            ?.let { key ->
                tasks.register<Exec>("distributeSigningGPGKey") {
                    executable = "../script/gpg/distribute-gpg.sh"

                    args(key)
                }
            }
    }
}
