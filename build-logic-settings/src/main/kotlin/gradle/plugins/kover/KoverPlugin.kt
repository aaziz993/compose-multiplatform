package gradle.plugins.kover

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kover.model.KoverSettings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.kover
                .takeIf(KoverSettings::enabled)?.let { kover ->
                    plugins.apply(project.settings.libs.plugin("kover").id)

                    kover.applyTo()

                    registerKoverReportTask()

                    if (kover.dependenciesFromSubprojects) {
                        configureDependencies()
                    }
                }
        }
    }

    private fun Project.configureDependencies() {
        val kover by configurations
        dependencies {
            subprojects.forEach { subproject ->
                kover(subproject)
            }
        }
    }

    private fun Project.registerKoverReportTask() {
        tasks.register("koverReport") {
            dependsOn(tasks.named("koverHtmlReport"), tasks.named("koverXmlReport"))
        }
    }
}
