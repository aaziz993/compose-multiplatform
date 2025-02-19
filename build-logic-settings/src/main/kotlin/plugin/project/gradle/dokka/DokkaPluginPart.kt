package plugin.project.gradle.dokka

import gradle.moduleProperties
import gradle.libs
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import plugin.project.BindingPluginPart
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import plugin.project.gradle.dokka.model.DokkaMultiModuleFileLayout
import plugin.project.gradle.dokka.model.DokkaTask

internal class DokkaPluginPart(override val project: Project) : BindingPluginPart {

    private val spotless by lazy {
        project.moduleProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    private val dokka by lazy {
        project.moduleProperties.settings.gradle.dokka
    }

    override fun applyAfterEvaluate() = with(project) {
        plugins.apply(project.libs.plugins.dokka.get().pluginId)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureDokkaExtension()

        if (dokka.versioning) {
            val dokkaPlugin by configurations

            dependencies {
                dokkaPlugin(libs.dokka.versioning)
            }
        }
    }

    @OptIn(InternalDokkaGradlePluginApi::class)
    private fun Project.configureDokkaModuleTask() =
        dokka.task?.let { task ->
            tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
                configureFrom(task)
            }
        }

    @OptIn(InternalDokkaGradlePluginApi::class)
    private fun Project.configureDokkaMultiModuleTask() =
        dokka.task?.let { task ->
            task as plugin.project.gradle.dokka.model.DokkaMultiModuleTask
            tasks.withType<DokkaMultiModuleTask> {
                configureFrom(task)
                task.includes?.let(includes::setFrom)
                fileLayout tryAssign task.fileLayout?.let(DokkaMultiModuleFileLayout::toDokkaMultiModuleFileLayout)
            }
        }

    private fun AbstractDokkaTask.configureFrom(config: DokkaTask) = apply {
        moduleName tryAssign config.moduleName
        moduleVersion tryAssign config.moduleVersion
        outputDirectory tryAssign config.outputDirectory?.let(project.layout.projectDirectory::dir)
        pluginsConfiguration tryAssign config.pluginsConfiguration
        pluginsMapConfiguration tryAssign config.pluginsMapConfiguration
        suppressObviousFunctions tryAssign config.suppressObviousFunctions
        suppressInheritedMembers tryAssign config.suppressInheritedMembers
        offlineMode tryAssign config.offlineMode
        failOnWarning tryAssign config.failOnWarning
        cacheRoot tryAssign config.cacheRoot?.let(project.layout.projectDirectory::dir)
    }
}
