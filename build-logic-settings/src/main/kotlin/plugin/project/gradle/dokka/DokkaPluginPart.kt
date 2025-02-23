package plugin.project.gradle.dokka

import gradle.libs
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import plugin.project.gradle.dokka.model.DokkaMultiModuleFileLayout

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
           projectProperties.plugins.dokka.let { dokka ->
                if (!dokka.enabled ||projectProperties.kotlin.hasTargets) {
                    return@with
                }

                plugins.apply(project.libs.plugins.dokka.get().pluginId)

                configureDokkaExtension()

                if (dokka.versioning) {
                    val dokkaPlugin by configurations

                    dependencies {
                        dokkaPlugin(libs.dokka.versioning)
                    }
                }

                if (project == rootProject) {
                    configureDokkaMultiModuleTask()
                }
                else {
                    configureDokkaModuleTask()
                }
            }
        }
    }

    @OptIn(InternalDokkaGradlePluginApi::class)
    private fun Project.configureDokkaModuleTask() =
       projectProperties.plugins.dokka.task?.let { task ->
            tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
                task.applyTo(this)
            }
        }

    @OptIn(InternalDokkaGradlePluginApi::class)
    private fun Project.configureDokkaMultiModuleTask() =
       projectProperties.plugins.dokka.task?.let { task ->
            tasks.withType<DokkaMultiModuleTask> {
                task.applyTo(this)
                task.includes?.let(includes::setFrom)
                fileLayout tryAssign task.fileLayout?.let(DokkaMultiModuleFileLayout::toDokkaMultiModuleFileLayout)
            }
        }
}
