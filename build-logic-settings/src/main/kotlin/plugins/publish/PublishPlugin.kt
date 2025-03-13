package plugins.publish

import gradle.accessors.kotlin
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.assign
import gradle.accessors.projectProperties
import gradle.accessors.publishing
import gradle.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.bundling.Jar
import org.gradle.internal.extensions.core.extra
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
                }
        }
    }

    private fun Project.configureJavadocArtifact() {
        val nonDefaultProjectStructure: List<String> by rootProject.extra
        if (project.name in nonDefaultProjectStructure) return

        val emptyJar = tasks.register<Jar>("emptyJar") {
            archiveAppendix = "empty"
        }

        for (target in kotlin.targets) {
            val publication = publishing.publications.findByName(target.name) as? MavenPublication ?: continue

            if (target.platformType.name == "jvm") {
                publication.artifact(emptyJar) {
                    classifier = "javadoc"
                }
            }
            else {
                publication.artifact(emptyJar) {
                    classifier = "javadoc"
                }
                publication.artifact(emptyJar) {
                    classifier = "kdoc"
                }
            }

            if (target.platformType.name == "native") {
                publication.artifact(emptyJar)
            }
        }

        // We share emptyJar artifact between all publications, so all publish tasks should be run after all sign tasks.
        // Otherwise Gradle will throw an error like:
        //   Task ':publishX' uses output of task ':signY' without declaring an explicit or implicit dependency.
        tasks.withType<AbstractPublishToMaven>().configureEach { mustRunAfter(tasks.withType<Sign>()) }
    }
}
