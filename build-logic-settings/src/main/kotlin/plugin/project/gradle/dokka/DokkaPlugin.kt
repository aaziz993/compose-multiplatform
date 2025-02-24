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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugin.project.gradle.dokka.model.DokkaSettings

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.dokka
                .takeIf { it.enabled && projectProperties.kotlin.hasTargets }?.let { dokka ->
                    plugins.apply(settings.libs.plugins.plugin("dokka").id)

                    dokka.applyTo()
                }
        }
    }
}
