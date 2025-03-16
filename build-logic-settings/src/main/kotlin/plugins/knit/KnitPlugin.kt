package plugins.knit

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.findByName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle

internal class KnitPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.knit
                .takeIf { it.enabled && project == rootProject }?.let { knit ->
                    plugins.apply(settings.libs.plugins.plugin("knit").id)

                    knit.applyTo()
                }
        }
    }

    companion object {

        context(Gradle)
        fun configureKnitTasks() {
            // In order for knit to operate, it should depend on and collect
            // all Dokka outputs from each module
            allprojects {
                if (project == project.rootProject) {
                    project.tasks.named("knitPrepare") {
                        dependsOn(dokkaTasks(project))
                    }
                }
            }
        }

        private fun dokkaTasks(project: Project): List<Task> =
            project.subprojects.takeIf(Set<*>::isNotEmpty)?.flatMap(::dokkaTasks)
                ?: listOfNotNull(project.tasks.findByName("dokkaGenerate"))
    }
}
