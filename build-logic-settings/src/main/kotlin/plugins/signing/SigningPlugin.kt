package plugins.signing

import org.gradle.kotlin.dsl.withType
import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.Sign
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

                    // NOTE: This is a temporary WA, see KT-61313.
                    tasks.withType<Sign> {
                        val pubName = name.substringAfter("sign").substringBefore("Publication")

                        // Task ':linkDebugTest<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
                        tasks.findByName("linkDebugTest$pubName")?.mustRunAfter(this)
                        // Task ':compileTestKotlin<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
                        tasks.findByName("compileTestKotlin$pubName")?.mustRunAfter(this)
                    }
                }
        }
    }

    private fun Project.registerGenerateSigningGPGKeyTask() = projectProperties.plugins.signing.generateGpg?.let { generateGpg ->
        tasks.register<Exec>("generateSigningGPGKey") {
            description = "Generates the signing GPG key"
            group = "signing"

            executable = settings.settingsDir.resolve("scripts/gpg/gen-gpg.sh").absolutePath

            args(
                generateGpg.keyType,
                generateGpg.keyLength,
                generateGpg.subkeyType,
                generateGpg.subkeyLength,
                generateGpg.nameReal ?: projectProperties.developer?.name!!,
                generateGpg.nameComment,
                generateGpg.nameEmail ?: projectProperties.developer?.email!!,
                generateGpg.expireDate,
                generateGpg.passphrase,
            )
        }

        tasks.register<Exec>("listSigningGPGKey") {
            description = "List the signing GPG keys"
            group = "signing"

            executable = settings.settingsDir.resolve("scripts/gpg/list-gpg.sh").absolutePath

            args(
                generateGpg.nameReal ?: projectProperties.developer?.name!!,
                generateGpg.passphrase,
            )
        }

        tasks.register<Exec>("cleanSigningGPGKey") {
            description = "Clean the signing GPG keys"
            group = "signing"

            executable = settings.settingsDir.resolve("scripts/gpg/clean-gpg.sh").absolutePath

            args(
                generateGpg.nameReal ?: projectProperties.developer?.name!!,
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
                    description = "Distributes the signing GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]"
                    group = "signing"

                    executable = settings.settingsDir.resolve("scripts/gpg/distribute-gpg.sh").absolutePath

                    args(key)
                }
            }
    }
}
