package gradle.plugins.publish

import gradle.api.findByName
import gradle.api.project.kotlin
import gradle.api.tasks.dokkaGeneratePublicationHtml
import gradle.api.tasks.dokkaGeneratePublicationJavadoc
import java.util.regex.Pattern
import klib.data.type.collections.associateWithNotNull
import klib.data.type.primitives.string.uppercaseFirstChar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

public class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("maven-publish") {
                configureJavadocArtifact()
                registerAggregatingPublishTasks()
                configurePublishTask()
            }
        }
    }

    private fun Project.configureJavadocArtifact() {
        val targetPublications = kotlin.targets.associateWithNotNull { target ->
            publishing.publications.findByName<MavenPublication>(target.name)
        }

        val javadocJar =
            if (pluginManager.hasPlugin("org.jetbrains.dokka-javadoc"))
                tasks.register<Jar>("dokkaJavadocJar") {
                    description = "A Javadoc JAR containing Dokka Javadoc"
                    from(tasks.dokkaGeneratePublicationJavadoc!!.flatMap(DokkaGeneratePublicationTask::outputDirectory))
                }
            else tasks.register<Jar>("javadocJar") { archiveAppendix = "empty" }

        targetPublications.forEach { (target, publication) ->
            publication.artifact(javadocJar) { classifier = "javadoc" }

            if (target.platformType.name != "jvm") {
                publication.artifact(javadocJar) { classifier = "kdoc" }
            }

            if (target.platformType.name == "native") {
                publication.artifact(javadocJar)
            }
        }

        if (pluginManager.hasPlugin("org.jetbrains.dokka")) {
            val dokkaHtmlJar by tasks.registering(Jar::class) {
                description = "A HTML Documentation JAR containing Dokka HTML"
                from(tasks.dokkaGeneratePublicationHtml!!.flatMap(DokkaGeneratePublicationTask::outputDirectory))
                archiveClassifier = "html-doc"
            }

            targetPublications.values.forEach { publication ->
                publication.artifact(dokkaHtmlJar)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.registerAggregatingPublishTasks() {
        registerAggregatingPublishTask<KotlinJvmTarget>("jvmAll")

        registerAggregatingPublishTask<KotlinAndroidTarget>("androidAll")

        // Android native
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNativeArm32All") { target -> target.konanTarget == KonanTarget.ANDROID_ARM32 }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNativeX86All") { target -> target.konanTarget == KonanTarget.ANDROID_X86 }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNative32") { target -> target.konanTarget in listOf(KonanTarget.ANDROID_ARM32, KonanTarget.ANDROID_X86) }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNativeArm64All") { target -> target.konanTarget == KonanTarget.ANDROID_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNativeX64All") { target -> target.konanTarget == KonanTarget.ANDROID_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNative64") { target -> target.konanTarget in listOf(KonanTarget.ANDROID_ARM64, KonanTarget.ANDROID_X64) }
        registerAggregatingPublishTask<KotlinNativeTarget>("androidNative") { target -> target.konanTarget.family == Family.ANDROID }

        // Darwin
        // ios
        registerAggregatingPublishTask<KotlinNativeTarget>("iosArm64All") { target -> target.konanTarget == KonanTarget.IOS_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("iosX64All") { target -> target.konanTarget == KonanTarget.IOS_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("iosSimulatorArm64All") { target -> target.konanTarget == KonanTarget.IOS_SIMULATOR_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("ios") { target -> target.konanTarget.family == Family.IOS }
        // watchos
        registerAggregatingPublishTask<KotlinNativeTarget>("watchosArm32All") { target -> target.konanTarget == KonanTarget.WATCHOS_ARM32 }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchosArm64All") { target -> target.konanTarget == KonanTarget.WATCHOS_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchosX64All") { target -> target.konanTarget == KonanTarget.WATCHOS_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchos64") { target -> target.konanTarget in listOf(KonanTarget.WATCHOS_ARM64, KonanTarget.WATCHOS_X64) }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchosDeviceArm64All") { target -> target.konanTarget == KonanTarget.WATCHOS_DEVICE_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchosSimulatorArm64All") { target -> target.konanTarget == KonanTarget.WATCHOS_SIMULATOR_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("watchos") { target -> target.konanTarget.family == Family.WATCHOS }
        // tvos
        registerAggregatingPublishTask<KotlinNativeTarget>("tvosArm64All") { target -> target.konanTarget == KonanTarget.TVOS_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("tvosX64All") { target -> target.konanTarget == KonanTarget.TVOS_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("tvosSimulatorArm64All") { target -> target.konanTarget == KonanTarget.TVOS_SIMULATOR_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("tvos") { target -> target.konanTarget.family == Family.TVOS }
        // macos
        registerAggregatingPublishTask<KotlinNativeTarget>("macosArm64All") { target -> target.konanTarget == KonanTarget.MACOS_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("macosX64All") { target -> target.konanTarget == KonanTarget.MACOS_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("macos") { target -> target.konanTarget.family == Family.OSX }
        // apple
        registerAggregatingPublishTask<KotlinNativeTarget>("apple") { target -> target.konanTarget.family in listOf(Family.IOS, Family.WATCHOS, Family.TVOS, Family.OSX) }

        // Linux
        registerAggregatingPublishTask<KotlinNativeTarget>("linuxArm64All") { target -> target.konanTarget == KonanTarget.LINUX_ARM64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("linuxX64All") { target -> target.konanTarget == KonanTarget.LINUX_X64 }
        registerAggregatingPublishTask<KotlinNativeTarget>("linux") { target -> target.konanTarget.family == Family.LINUX }

        // Windows
        registerAggregatingPublishTask<KotlinNativeTarget>("mingwX64All") { target -> target.konanTarget == KonanTarget.MINGW_X64 }

        // Native
        registerAggregatingPublishTask<KotlinNativeTarget>("native")

        registerAggregatingPublishTask<KotlinTarget>("jsAll") { target -> target::class == KotlinJsTargetDsl::class }

        registerAggregatingPublishTask<KotlinWasmTargetDsl>("wasmAll")

        registerAggregatingPublishTask<KotlinJsTargetDsl>("web")

        registerAggregatingPublishTask<KotlinWasmWasiTargetDsl>("wasmWasiAll")
    }

    private inline fun <reified T : KotlinTarget> Project.registerAggregatingPublishTask(
        name: String,
        noinline predicate: (T) -> Boolean = { true }
    ) = registerAggregatingPublishTask(
        name,
        kotlin.targets.withType<T>().matching(predicate).map(KotlinTarget::targetName),
    )

    private fun Project.registerAggregatingPublishTask(name: String, targetNames: List<String>) {
        if (targetNames.isEmpty()) return

        publishing.repositories.forEach { repository ->
            val capitalizedRepositoryName = repository.name.uppercaseFirstChar()

            tasks.matching { task ->
                targetNames.any { targetName ->
                    task.name.matches(
                        "${
                            Pattern.quote("publish${targetName.uppercaseFirstChar()}")
                        }.*?${
                            Pattern.quote("PublicationTo${capitalizedRepositoryName}Repository")
                        }Repository".toRegex(),
                    )
                }
            }.takeIf(TaskCollection<*>::isNotEmpty)?.let { publishTasks ->
                tasks.register("publish${name.uppercaseFirstChar()}PublicationTo${capitalizedRepositoryName}Repository") {
                    group = PublishingPlugin.PUBLISH_TASK_GROUP

                    dependsOn(publishTasks)
                }
            }
        }
    }

    private fun Project.configurePublishTask() {
        // We share emptyJar artifact between all publications, so all publish tasks should be run after all sign tasks.
        // Otherwise, Gradle will throw an error like:
        //   Task ':publishX' uses output of task ':signY' without declaring an explicit or implicit dependency.
        tasks.withType<AbstractPublishToMaven>().configureEach { mustRunAfter(tasks.withType<Sign>()) }

        tasks.named("publish") {
            dependsOn(tasks.named("publishToMavenLocal"))
        }
    }
}
