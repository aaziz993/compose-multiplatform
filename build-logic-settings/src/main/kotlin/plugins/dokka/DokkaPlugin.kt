package plugins.dokka

import org.gradle.kotlin.dsl.getValue
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.dokka.gradle.DokkaTask
import plugins.dokka.model.DokkaSettings

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.dokka
                .takeIf(DokkaSettings::enabled)?.let { dokka ->
                    plugins.apply(settings.libs.plugins.plugin("dokka").id)
                    plugins.apply(settings.libs.plugins.plugin("dokkaJavadoc").id)

                    dokka.applyTo()

                    if (project == rootProject && dokka.dependenciesFromSubprojects) {
                        configureDependencies()
                    }
                }
        }
    }

    private fun Project.configureDependencies() {
        val dokka by configurations
        dependencies {
            subprojects.forEach { subproject ->
                dokka(subproject)
            }
        }
    }
}
