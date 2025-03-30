package gradle.plugins.signing

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign

internal class SigningPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.signing?.applyTo()

            project.pluginManager.withPlugin("signing") {
                // NOTE: This is a temporary WA, see KT-61313.
                configureSignTask()
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
    private fun Project.configureSignTask() {
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
}
