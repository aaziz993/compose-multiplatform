package plugins.signing

import org.gradle.kotlin.dsl.withType
import gradle.accessors.projectProperties
import gradle.accessors.resolveValue
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
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

                    // NOTE: This is a temporary WA, see KT-61313.
                    establishSignDependencies()

                    registerGenerateSigningGPGKeyTasks()
                }
        }
    }

    /**
     * This unbelievable piece of engineering^W programming is a workaround for the following issues:
     * - https://github.com/gradle/gradle/issues/26132
     * - https://youtrack.jetbrains.com/issue/KT-61313/
     *
     * Long story short:
     * 1) Single module produces multiple publications
     * 2) 'Sign' plugin signs them
     * 3) Signature files are re-used, which Gradle detects and whines about an implicit dependency
     *
     * There are three patterns that we workaround:
     * 1) 'Sign' does not depend on 'publish'
     * 2) Empty 'javadoc.jar.asc' got reused between publications (kind of a implication of the previous one)
     * 3) `klib` signatures are reused where appropriate
     *
     * It addresses the following failures:
     * ```
     * Gradle detected a problem with the following location: 'kotlinx.coroutines/kotlinx-coroutines-core/build/classes/kotlin/macosArm64/main/klib/kotlinx-coroutines-core.klib.asc'.
     * Reason: Task ':kotlinx-coroutines-core:linkWorkerTestDebugTestMacosArm64' uses this output of task ':kotlinx-coroutines-core:signMacosArm64Publication' without declaring an explicit or implicit dependency. This can lead to incorrect results being produced, depending on what order the tasks are executed.
     *
     * ```
     * and
     * ```
     * Gradle detected a problem with the following location: 'kotlinx-coroutines-core/build/libs/kotlinx-coroutines-core-1.7.2-SNAPSHOT-javadoc.jar.asc'.
     * Reason: Task ':kotlinx-coroutines-core:publishAndroidNativeArm32PublicationToMavenLocal' uses this output of task ':kotlinx-coroutines-core:signAndroidNativeArm64Publication' without declaring an explicit or implicit dependency.
     * ```
     */
    fun Project.establishSignDependencies() {
        tasks.withType<Sign>().configureEach {
            val pubName = name.removePrefix("sign").removeSuffix("Publication")
            // Gradle#26132 -- establish dependency between sign and link tasks, as well as compile ones
            // Task ':linkDebugTest<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
            mustRunAfter(tasks.matching { it.name == "linkDebugTest$pubName" })
            mustRunAfter(tasks.matching { it.name == "linkWorkerTestDebugTest$pubName" })
            // Task ':compileTestKotlin<platform>' uses this output of task ':sign<platform>Publication' without declaring an explicit or implicit dependency
            mustRunAfter(tasks.matching { it.name == "compileTestKotlin$pubName" })
        }

        // Sign plugin issues and publication:
        // Establish dependency between 'sign' and 'publish*' tasks
        tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(tasks.withType<Sign>())
        }
    }

    private fun Project.registerGenerateSigningGPGKeyTasks() {
        /** Distribute signing gpg key
         * There are 3 servers supported by Central servers: [ keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu ]
         */
        projectProperties.plugins.signing.useInMemoryPgpKeys?.defaultSecretKey
            ?.resolveValue()
            ?.let { key ->
                tasks.register<Exec>("distributeSigningGPGKey") {
                    description = "Distributes the signing GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]"
                    group = "signing"

                    executable = settings.settingsDir.resolve("scripts/gpg/distribute-gpg-key.sh").absolutePath

                    args(key)
                }
            }
        projectProperties.plugins.signing.generateGpg?.let { generateGpg ->
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
