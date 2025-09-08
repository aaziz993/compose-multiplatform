package gradle.plugins.knit

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
                adjustKnitTask()
            }
        }
    }

    private fun Project.adjustKnitTask() = project.pluginManager.withPlugin("org.jetbrains.dokka") {
        tasks.named("knitPrepare") {
            // In order for knit to operate, it should depend on and collect.
            // all Dokka outputs from each module.
            dependsOn(tasks.named("dokkaGenerate"))
        }
    }
}
