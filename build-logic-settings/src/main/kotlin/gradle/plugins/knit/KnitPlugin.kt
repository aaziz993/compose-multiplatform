package gradle.plugins.knit

import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply knit properties.
            projectProperties.knit?.applyTo()

            project.pluginManager.withPlugin("org.jetbrains.kotlinx.knit") {
                project.pluginManager.withPlugin("org.jetbrains.dokka") {
                    tasks.named("knitPrepare") {
                        // In order for knit to operate, it should depend on and collect
                        // all Dokka outputs from each module
                        dependsOn(tasks.named("dokkaGenerate"))
                    }
                }
            }
        }
    }
}
