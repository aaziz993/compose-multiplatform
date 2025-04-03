package gradle.plugins.kover

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            //Apply kover properties.
            projectProperties.kover?.applyTo()

            pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
                registerKoverReportTask()
            }
        }
    }

    private fun Project.registerKoverReportTask() {
        tasks.register("koverReport") {
            dependsOn(tasks.named("koverHtmlReport"), tasks.named("koverXmlReport"))
        }
    }
}
