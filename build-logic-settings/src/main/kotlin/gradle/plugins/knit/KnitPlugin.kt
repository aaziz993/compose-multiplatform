package gradle.plugins.knit

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.knit.model.KnitSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.knit
                .takeIf(KnitSettings::enabled)?.let { knit ->
                    plugins.apply(project.settings.libs.plugins.plugin("knit").id)

                    knit.applyTo()

                    tasks.named("knitPrepare") {
                        // In order for knit to operate, it should depend on and collect
                        // all Dokka outputs from each module
                        dependsOn(tasks.named("dokkaGenerate"))
                    }
                }
        }
    }
}
