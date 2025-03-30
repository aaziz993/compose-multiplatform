package gradle.plugins.publish

import gradle.accessors.catalog.libs
import gradle.accessors.dokkaGeneratePublicationHtml
import gradle.accessors.dokkaGeneratePublicationJavadoc
import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.accessors.publishing
import gradle.accessors.settings
import gradle.api.findByName
import gradle.collection.associateWithNotNull
import gradle.plugins.kotlin.filterKotlinTargets
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNative32Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNative64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeArm32Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeArm64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeTarget
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeX64Target
import gradle.plugins.kotlin.targets.nat.android.KotlinAndroidNativeX86Target
import gradle.plugins.kotlin.targets.nat.apple.KotlinAppleTarget
import gradle.plugins.kotlin.targets.nat.apple.ios.IosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosTarget
import gradle.plugins.kotlin.targets.nat.apple.ios.KotlinIosX64Target
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosTarget
import gradle.plugins.kotlin.targets.nat.apple.macos.KotlinMacosX64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosTarget
import gradle.plugins.kotlin.targets.nat.apple.tvos.KotlinTvosX64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchos32Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchos64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosArm32Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosDeviceArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosSimulatorArm64Target
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosTarget
import gradle.plugins.kotlin.targets.nat.apple.watchos.KotlinWatchosX64Target
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxArm64Target
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxTarget
import gradle.plugins.kotlin.targets.nat.linux.KotlinLinuxX64Target
import gradle.plugins.kotlin.targets.nat.mingw.KotlinMingwTarget
import gradle.plugins.kotlin.targets.nat.mingw.KotlinMingwX64Target
import gradle.plugins.publish.model.PublishingSettings
import java.util.regex.Pattern
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.publishing?.takeIf{ pluginManager.hasPlugin("publishing") }?.let { publishing ->
                    plugins.apply(MavenPublishPlugin::class.java)

                    publishing.applyTo()

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
            if (plugins.hasPlugin(project.settings.libs.plugin("dokka").id))
                tasks.register<Jar>("dokkaJavadocJar") {
                    description = "A Javadoc JAR containing Dokka Javadoc"
                    from(tasks.dokkaGeneratePublicationJavadoc!!.flatMap { it.outputDirectory })
                }
            else
                tasks.register<Jar>("javadocJar") {
                    archiveAppendix = "empty"
                }

        targetPublications.forEach { (target, publication) ->
            publication.artifact(javadocJar) { classifier = "javadoc" }

            if (target.platformType.name != "jvm") {
                publication.artifact(javadocJar) { classifier = "kdoc" }
            }

            if (target.platformType.name == "native") {
                publication.artifact(javadocJar)
            }
        }

        if (plugins.hasPlugin(project.settings.libs.plugin("dokkaJavadoc").id)) {
            val dokkaHtmlJar by tasks.registering(Jar::class) {
                description = "A HTML Documentation JAR containing Dokka HTML"
                from(tasks.dokkaGeneratePublicationHtml!!.flatMap { it.outputDirectory })
                archiveClassifier = "html-doc"
            }

            targetPublications.values.forEach { publication ->
                publication.artifact(dokkaHtmlJar)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun Project.registerAggregatingPublishTasks() {
        registerAggregatingPublishTask(
            "jvmAll",
        ) { it is KotlinJvmTarget }

        registerAggregatingPublishTask(
            "androidAll",
        ) { it is KotlinAndroidTarget }

        // Android native
        registerAggregatingNativePublishTask<KotlinAndroidNativeArm32Target>("androidNativeArm32All")
        registerAggregatingNativePublishTask<KotlinAndroidNativeX86Target>("androidNativeX86All")
        registerAggregatingNativePublishTask<KotlinAndroidNative32Target>("androidNative32")
        registerAggregatingNativePublishTask<KotlinAndroidNativeArm64Target>("androidNativeArm64All")
        registerAggregatingNativePublishTask<KotlinAndroidNativeX64Target>("androidNativeX64All")
        registerAggregatingNativePublishTask<KotlinAndroidNative64Target>("androidNative64")
        registerAggregatingNativePublishTask<KotlinAndroidNativeTarget>("androidNative")

        // Darwin
        // ios
        registerAggregatingNativePublishTask<IosArm64Target>("iosArm64All")
        registerAggregatingNativePublishTask<KotlinIosX64Target>("iosX64All")
        registerAggregatingNativePublishTask<KotlinIosSimulatorArm64Target>("iosSimulatorArm64All")
        registerAggregatingNativePublishTask<KotlinIosTarget>("ios")
        // watchos
        registerAggregatingNativePublishTask<KotlinWatchosArm32Target>("watchosArm32All")
        registerAggregatingNativePublishTask<KotlinWatchosArm64Target>("watchosArm64All")
        registerAggregatingNativePublishTask<KotlinWatchos32Target>("watchos32")
        registerAggregatingNativePublishTask<KotlinWatchosDeviceArm64Target>("watchosDeviceArm64All")
        registerAggregatingNativePublishTask<KotlinWatchosX64Target>("watchosX64All")
        registerAggregatingNativePublishTask<KotlinWatchosSimulatorArm64Target>("watchosSimulatorArm64All")
        registerAggregatingNativePublishTask<KotlinWatchos64Target>("watchos64")
        registerAggregatingNativePublishTask<KotlinWatchosTarget>("watchos")
        // tvos
        registerAggregatingNativePublishTask<KotlinTvosArm64Target>("tvosArm64All")
        registerAggregatingNativePublishTask<KotlinTvosX64Target>("tvosX64All")
        registerAggregatingNativePublishTask<KotlinTvosSimulatorArm64Target>("tvosSimulatorArm64All")
        registerAggregatingNativePublishTask<KotlinTvosTarget>("tvos")
        // macos
        registerAggregatingNativePublishTask<KotlinMacosTarget>("macos")
        registerAggregatingNativePublishTask<KotlinMacosArm64Target>("macosArm64All")
        registerAggregatingNativePublishTask<KotlinMacosX64Target>("macosX64All")
        registerAggregatingNativePublishTask<KotlinMacosTarget>("macos")
        // apple
        registerAggregatingNativePublishTask<KotlinAppleTarget>("apple")

        // Linux
        registerAggregatingNativePublishTask<KotlinLinuxArm64Target>("linuxArm64All")
        registerAggregatingNativePublishTask<KotlinLinuxX64Target>("linuxX64All")
        registerAggregatingNativePublishTask<KotlinLinuxTarget>("linux")

        // Windows
        registerAggregatingNativePublishTask<KotlinMingwTarget>("mingw")
        registerAggregatingNativePublishTask<KotlinMingwX64Target>("mingwX64All")

        // Native
        registerAggregatingPublishTask(
            "native",
        ) { it is KotlinNativeTarget }

        registerAggregatingPublishTask(
            "jsAll",
        ) { it::class == KotlinJsTargetDsl::class }

        registerAggregatingPublishTask(
            "wasmAll",
        ) { it is KotlinWasmTargetDsl }

        registerAggregatingPublishTask(
            "jsCommon",
        ) { it is KotlinJsTargetDsl }

        registerAggregatingPublishTask(
            "wasmWasiAll",
        ) { it is KotlinWasmWasiTargetDsl }
    }

    private fun Project.registerAggregatingPublishTask(
        name: String,
        targetFilter: (KotlinTarget) -> Boolean,
    ) =
        registerAggregatingPublishTask(
            name,
            kotlin.targets.matching(targetFilter).map(KotlinTarget::targetName),
        )

    private inline fun <reified T : Any> Project.registerAggregatingNativePublishTask(
        name: String
    ) =
        registerAggregatingPublishTask(
            name,
            projectProperties.kotlin.targets
                .filterKotlinTargets<T>().mapNotNull(`gradle.plugins.kotlin`.KotlinTarget<*>::targetName),
        )

    private fun Project.registerAggregatingPublishTask(
        name: String,
        targetNames: List<String>,
    ) {
        if (targetNames.isEmpty()) return

        publishing.repositories.forEach { repository ->
            val capitalizedRepositoryName = repository.name.capitalized()

            tasks.matching { task ->
                targetNames.any { targetName ->
                    task.name.matches(
                        "${
                            Pattern.quote("publish${targetName.capitalized()}")
                        }.*?${
                            Pattern.quote("PublicationTo${capitalizedRepositoryName}Repository")
                        }Repository".toRegex(),
                    )
                }
            }.takeIf(TaskCollection<*>::isNotEmpty)?.let { publishTasks ->
                tasks.register("publish${name.capitalized()}PublicationTo${capitalizedRepositoryName}Repository") {
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
