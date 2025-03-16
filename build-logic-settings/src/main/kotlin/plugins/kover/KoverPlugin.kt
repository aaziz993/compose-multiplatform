package plugins.kover

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugins.kover.model.KoverSettings

internal class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.kover
                .takeIf(KoverSettings::enabled)?.let { kover ->

                    plugins.apply(settings.libs.plugins.plugin("kover").id)

                    kover.applyTo()

                    registerKoverReportTask()

                    if (project == rootProject && kover.dependenciesFromIncludes) {
                        val kover by configurations
                        dependencies {
                            subprojects.forEach { subproject ->
                                kover(subproject)
                            }
                        }
                    }
                }
        }
    }

    private fun Project.registerKoverReportTask() {
        tasks.register("koverReport") {
            dependsOn(tasks.named("koverHtmlReport"))
            dependsOn(tasks.named("koverXmlReport"))
        }
    }
}
