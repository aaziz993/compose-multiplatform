package gradle.plugins.signing.model

import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.signing.InMemoryPgpKeys
import gradle.plugins.signing.SignContentPolymorphicSerializer
import gradle.plugins.signing.SigningExtension
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

@Serializable
internal data class SigningSettings(
    override val required: Boolean? = null,
    override val useGpgCmd: Boolean? = null,
    override val useInMemoryPgpKeys: InMemoryPgpKeys? = null,
    override val sign: Set<@Serializable(with = SignContentPolymorphicSerializer::class) Any>? = null,
    val generateGpg: GenerateGgp? = null,
) : SigningExtension() {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin("signing") {
            super.applyTo()

            project.registerGenerateSigningGPGKeyTasks()
        }

    private fun Project.registerGenerateSigningGPGKeyTasks() {
        /** Distribute signing gpg key
         * There are 3 servers supported by Central servers: [ keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu ]
         */
        useInMemoryPgpKeys?.defaultSecretKey?.let { key ->
            tasks.register<Exec>("distributeSigningGPGKey") {
                description = "Distributes the signing GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]"
                group = "signing"

                executable = settings.settingsDir.resolve("scripts/gpg/distribute-gpg-key.sh").absolutePath

                args(key)
            }
        }

        generateGpg?.let { generateGpg ->
            tasks.register<Exec>("generateSigningGPGKey") {
                description = "Generates the signing GPG key"
                group = "signing"

                executable = settings.settingsDir.resolve("scripts/gpg/generate-gpg-key.sh").absolutePath

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

                executable = settings.settingsDir.resolve("scripts/gpg/list-gpg_keys.sh").absolutePath

                args(
                    generateGpg.nameReal ?: projectProperties.developer?.name!!,
                    generateGpg.passphrase,
                )
            }

            tasks.register<Exec>("cleanSigningGPGKey") {
                description = "Clean the signing GPG keys"
                group = "signing"

                executable = settings.settingsDir.resolve("scripts/gpg/clean-gpg-keys.sh").absolutePath

                args(
                    generateGpg.nameReal ?: projectProperties.developer?.name!!,
                )
            }
        }
    }
}
