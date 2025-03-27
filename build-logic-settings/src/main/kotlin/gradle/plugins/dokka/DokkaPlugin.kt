package gradle.plugins.dokka

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.dokka.model.DokkaSettings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.dokka
                .takeIf(DokkaSettings::enabled)?.let { dokka ->
                    plugins.apply(project.settings.libs.plugins.plugin("dokka").id)
                    plugins.apply(project.settings.libs.plugins.plugin("dokkaJavadoc").id)

                    dokka.applyTo()

                    if (dokka.dependenciesFromSubprojects) {
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
