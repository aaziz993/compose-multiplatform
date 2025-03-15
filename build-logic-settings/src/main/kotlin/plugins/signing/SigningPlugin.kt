package plugins.signing

import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
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

                    registerGenerateSigningGPGKeyTask()
                    registerSigningGPGKeyDistributeTask()
                }
        }
    }

    private fun Project.registerGenerateSigningGPGKeyTask() = projectProperties.plugins.signing.generateGpg { generateGpg ->
        tasks.register<Exec>("generateSigningGPGKey") {
            description = 'Generates a GPG key'
            group = 'signing'

            executable = settings.settingsDir.resolve("scripts/gpg/gen-gpg.sh").absolutePath

            args(
                generateGpg.keyType,
                generateGpg.keyLength,
                generateGpg.subkeyType,
                generateGpg.subkeyLength,
                generateGpg.nameReal,
                generateGpg.nameComment,
                generateGpg.nameEmail,
                generateGpg.expireDate,
                generateGpg.passphrase,
            )
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
                    description = 'Distrbute a GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]'
                    group = 'signing'

                    executable = settings.settingsDir.resolve("scripts/gpg/distribute-gpg.sh").absolutePath

                    args(key)
                }
            }
    }
}
