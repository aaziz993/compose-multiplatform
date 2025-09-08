package gradle.plugins.kover

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KoverPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
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
