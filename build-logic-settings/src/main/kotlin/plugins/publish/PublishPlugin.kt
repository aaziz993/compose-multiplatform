package plugins.publish

import gradle.accessors.kotlin
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.assign
import gradle.accessors.projectProperties
import gradle.accessors.publishing
import gradle.api.findByName
import gradle.api.maybeNamed
import gradle.api.maybeRegister
import gradle.plugins.kmp.KotlinJvmAndAndroidTarget
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.nat.android.KotlinAndroidNative
import gradle.plugins.kmp.nat.apple.KotlinAppleTarget
import gradle.plugins.kmp.nat.apple.macos.KotlinMacosTarget
import gradle.plugins.kmp.nat.linux.KotlinLinuxTarget
import gradle.plugins.kmp.nat.mingw.KotlinMingwTarget
import gradle.plugins.kmp.web.KotlinJsTargetDsl
import gradle.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.plugins.signing.Sign

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
        kotlin.targetHierarchy.
        // Jvm and Android
        registerAggregatingTask(
            "jvmCommon",
            projectProperties.kotlin.targets
                .filterIsInstance<KotlinJvmAndAndroidTarget>()
                .map(KotlinTarget::targetName).toSet(),
        )

        // Android native
        registerAggregatingTask(
            "AndroidNative",
            (projectProperties.kotlin.targets
                .filterIsInstance<KotlinAndroidNative>() as List<KotlinTarget>)
                .map(KotlinTarget::targetName).toSet(),
        )

        // Apple
        registerAggregatingTask(
            "Apple",
            (projectProperties.kotlin.targets
                .filterIsInstance<KotlinAppleTarget>() as List<KotlinTarget>)
                .map(KotlinTarget::targetName).toSet(),
        )

        // MacOS
        registerAggregatingTask(
            "MacOS",
            (projectProperties.kotlin.targets
                .filterIsInstance<KotlinMacosTarget>() as List<KotlinTarget>)
                .map(KotlinTarget::targetName).toSet(),
        )

        // Linux
        registerAggregatingTask(
            "Linux",
            (projectProperties.kotlin.targets
                .filterIsInstance<KotlinLinuxTarget>() as List<KotlinTarget>)
                .map(KotlinTarget::targetName).toSet(),
        )

        // Windows
        registerAggregatingTask(
            "Mingw",
            (projectProperties.kotlin.targets
                .filterIsInstance<KotlinMingwTarget>() as List<KotlinTarget>)
                .map(KotlinTarget::targetName).toSet(),
        )

        // Js and WasmJs
        registerAggregatingTask(
            "jsCommon",
            projectProperties.kotlin.targets
                .filterIsInstance<KotlinJsTargetDsl>()
                .map(KotlinTarget::targetName).toSet(),
        )
    }

    private fun Project.registerAggregatingTask(name: String, aliases: Set<String>) {
        if (aliases.isEmpty()) return

        tasks.maybeRegister("publish${name}PublicationToMavenRepository") {
            group = PublishingPlugin.PUBLISH_TASK_GROUP
            val targetsTasks = aliases.mapNotNull { target ->
                tasks.maybeNamed("publish${target.capitalized()}PublicationToMavenRepository")
            }
            dependsOn(targetsTasks)
        }
    }

    private fun Project.configureTasks() {
        // We share emptyJar artifact between all publications, so all publish tasks should be run after all sign tasks.
        // Otherwise, Gradle will throw an error like:
        //   Task ':publishX' uses output of task ':signY' without declaring an explicit or implicit dependency.
        tasks.withType<AbstractPublishToMaven>().configureEach { mustRunAfter(tasks.withType<Sign>()) }

        tasks.named("publish") {
            dependsOn(tasks.named("publishToMavenLocal"))
        }
    }
}
