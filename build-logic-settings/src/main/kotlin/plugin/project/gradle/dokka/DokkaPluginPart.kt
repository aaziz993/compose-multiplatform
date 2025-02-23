package plugin.project.gradle.dokka

import gradle.id
import gradle.libraries
import gradle.library
import gradle.libs
import gradle.module
import gradle.plugin
import gradle.plugins
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
                if (!dokka.enabled || !projectProperties.kotlin.hasTargets) {
                    return@with
                }

                plugins.apply(settings.libs.plugins.plugin("dokka").id)

                configureDokkaExtension()

                if (dokka.versioning) {
                    val dokkaPlugin by configurations

                    dependencies {
                        dokkaPlugin(settings.libs.libraries.library("dokka.versioning").module)
                    }
                }

                dokka.task?.applyTo()
            }
        }
    }
}
