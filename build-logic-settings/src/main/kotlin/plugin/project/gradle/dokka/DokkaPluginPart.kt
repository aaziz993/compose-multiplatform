package plugin.project.gradle.dokka

import gradle.moduleProperties
import gradle.libs
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.gradle.api.Plugin
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import plugin.project.gradle.dokka.model.DokkaMultiModuleFileLayout
import plugin.project.gradle.dokka.model.DokkaTask

internal class DokkaPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            moduleProperties.settings.gradle.dokka.let { dokka ->
                if (!dokka.enabled || moduleProperties.targets.isEmpty()) {
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
        moduleProperties.settings.gradle.dokka.task?.let { task ->
            tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
                task.applyTo(this)
            }
        }

    @OptIn(InternalDokkaGradlePluginApi::class)
    private fun Project.configureDokkaMultiModuleTask() =
        moduleProperties.settings.gradle.dokka.task?.let { task ->
            tasks.withType<DokkaMultiModuleTask> {
                task.applyTo(this)
                task.includes?.let(includes::setFrom)
                fileLayout tryAssign task.fileLayout?.let(DokkaMultiModuleFileLayout::toDokkaMultiModuleFileLayout)
            }
        }
}
