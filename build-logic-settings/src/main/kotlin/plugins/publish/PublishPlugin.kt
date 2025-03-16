package plugins.publish

import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.accessors.publishing
import gradle.api.findByName
import gradle.api.maybeNamed
import gradle.api.maybeRegister
import gradle.plugins.kmp.instanceOf
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeTarget
import gradle.plugins.kmp.nat.android.KotlinAndroidNative32Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNative64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeArm32Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeArm64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeX64Target
import gradle.plugins.kmp.nat.android.KotlinAndroidNativeX86Target
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
import gradle.plugins.kmp.nat.apple.ios.IosArm64Target
import gradle.plugins.kmp.nat.apple.ios.KotlinIosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.ios.KotlinIosTarget
import gradle.plugins.kmp.nat.apple.ios.KotlinIosX64Target
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosArm64Target
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosTarget
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosX64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosArm64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosTarget
import gradle.plugins.kmp.nat.apple.tvos.KotlinTvosX64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchos32Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchos64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosArm32Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosDeviceArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosSimulatorArm64Target
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosTarget
import gradle.plugins.kmp.nat.apple.watchos.KotlinWatchosX64Target
import gradle.plugins.kmp.nat.linux.KotlinLinuxArm64Target
import gradle.plugins.kmp.nat.linux.KotlinLinuxTarget
import gradle.plugins.kmp.nat.linux.KotlinLinuxX64Target
import gradle.plugins.kmp.nat.mingw.KotlinMingwTarget
import gradle.plugins.kmp.nat.mingw.KotlinMingwX64Target
import gradle.plugins.kmp.web.KotlinJsTarget
import gradle.plugins.kmp.web.KotlinWasmJsTarget
import gradle.project.ProjectType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.publishing
                .takeIf {
                    it.enabled && projectProperties.kotlin.targets.isNotEmpty() && projectProperties.type == ProjectType.LIB
                }?.let { publishing ->
                    plugins.apply(MavenPublishPlugin::class.java)

                    publishing.applyTo()

                    configureJavadocArtifact()

                    registerAggregatingTasks()

                    configureTasks()
                }
        }
    }

    private fun Project.configureJavadocArtifact() {
        val emptyJar = tasks.register<Jar>("emptyJar") {
            archiveAppendix = "empty"
        }

        for (target in kotlin.targets) {
            val publication = publishing.publications.findByName<MavenPublication>(target.name) ?: continue

            publication.artifact(emptyJar) { classifier = "javadoc" }

            if (target.platformType.name != "jvm") {
                publication.artifact(emptyJar) { classifier = "kdoc" }
            }

            if (target.platformType.name == "native") {
                publication.artifact(emptyJar)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.registerAggregatingTasks() {
        registerAggregatingTask(
            "jvmAll",
            KotlinJvmTarget::class,
        )

        // Android native
        registerAggregatingTask<KotlinAndroidNativeArm32Target>("androidNativeArm32All")
        registerAggregatingTask<KotlinAndroidNativeX86Target>("androidNativeX86All")
        registerAggregatingTask<KotlinAndroidNative32Target>("androidNative32")
        registerAggregatingTask<KotlinAndroidNativeArm64Target>("androidNativeArm64All")
        registerAggregatingTask<KotlinAndroidNativeX64Target>("androidNativeX64All")
        registerAggregatingTask<KotlinAndroidNative64Target>("androidNative64")
        registerAggregatingTask<KotlinAndroidNativeTarget>("androidNative")

        // Darwin
        // ios
        registerAggregatingTask<IosArm64Target>("iosArm64All")
        registerAggregatingTask<KotlinIosX64Target>("iosX64All")
        registerAggregatingTask<KotlinIosSimulatorArm64Target>("iosSimulatorArm64All")
        registerAggregatingTask<KotlinIosTarget>("ios")
        // watchos
        registerAggregatingTask<KotlinWatchosArm32Target>("watchosArm32All")
        registerAggregatingTask<KotlinWatchosArm64Target>("watchosArm64All")
        registerAggregatingTask<KotlinWatchos32Target>("watchos32")
        registerAggregatingTask<KotlinWatchosDeviceArm64Target>("watchosDeviceArm64All")
        registerAggregatingTask<KotlinWatchosX64Target>("watchosX64All")
        registerAggregatingTask<KotlinWatchosSimulatorArm64Target>("watchosSimulatorArm64All")
        registerAggregatingTask<KotlinWatchos64Target>("watchos64")
        registerAggregatingTask<KotlinWatchosTarget>("watchos")
        // tvos
        registerAggregatingTask<KotlinTvosArm64Target>("tvosArm64All")
        registerAggregatingTask<KotlinTvosX64Target>("tvosX64All")
        registerAggregatingTask<KotlinTvosSimulatorArm64Target>("tvosSimulatorArm64All")
        registerAggregatingTask<KotlinTvosTarget>("tvos")
        // macos
        registerAggregatingTask<KotlinMacosTarget>("macos")
        registerAggregatingTask<KotlinMacosArm64Target>("macosArm64All")
        registerAggregatingTask<KotlinMacosX64Target>("macosX64All")
        registerAggregatingTask<KotlinMacosTarget>("macos")
        // apple
        registerAggregatingTask<KotlinAppleTarget>("apple")

        // Linux
        registerAggregatingTask<KotlinLinuxArm64Target>("linuxArm64All")
        registerAggregatingTask<KotlinLinuxX64Target>("linuxX64All")
        registerAggregatingTask<KotlinLinuxTarget>("linux")

        // Windows
        registerAggregatingTask<KotlinMingwTarget>("mingw")
        registerAggregatingTask<KotlinMingwX64Target>("mingwX64All")

        // Native
        registerAggregatingTask("native", KotlinNativeTarget::class)

        // All js
        registerAggregatingTask<KotlinJsTarget>("jsAll")

        // All js
        registerAggregatingTask<KotlinWasmJsTarget>("wasmJsAll")

        // Js and wasmJs
        registerAggregatingTask("jsCommon", KotlinJsTargetDsl::class)

        // All wasmWasi
        registerAggregatingTask("wasmWasiAll", KotlinWasmWasiTargetDsl::class)
    }

    private inline fun <reified T : Any> Project.registerAggregatingTask(name: String) =
        registerAggregatingTask(
            name,
            projectProperties.kotlin.targets
                .instanceOf<T>()
                .map(`gradle.plugins.kmp`.KotlinTarget::targetName),
        )

    private fun <T : KotlinTarget> Project.registerAggregatingTask(name: String, vararg targetsKClasses: KClass<T>) =
        registerAggregatingTask(
            name,
            kotlin.targets.filter { target ->
                targetsKClasses.any { targetKClass -> target::class.isSubclassOf(targetKClass) }
            }.map(KotlinTarget::targetName),
        )

    private fun Project.registerAggregatingTask(name: String, aliases: List<String>) {
        if (aliases.isEmpty()) return

        publishing.repositories.forEach { repository ->
            val repositoryName = repository.name.capitalized()

            val publishTasks = aliases.mapNotNull { alias ->
                tasks.maybeNamed("publish${alias.capitalized()}PublicationTo${repositoryName}Repository")
            }

            if (publishTasks.isEmpty()) {
                return@forEach
            }

            tasks.register("publish${name}PublicationTo${repositoryName}Repository") {
                group = PublishingPlugin.PUBLISH_TASK_GROUP

                dependsOn(publishTasks)
            }
        }
    }

    private fun Project.configureTasks() {
        // We share emptyJar artifact between all publications, so all publish tasks should be run after all sign tasks.
        // Otherwise, Gradle will throw an error like:
        //   Task ':publishX' uses output of task ':signY' without declaring an explicit or implicit dependency.
        tasks.withType<AbstractPublishToMaven>().configureEach { mustRunAfter(tasks.withType<Sign>()) }

        tasks.named("publish") {
            dependsOn(tasks.named("publishToMavenLocal"), tasks.named("publishAllPublicationsToBuildRepoRepository"))
        }
    }
}
