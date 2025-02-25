package plugin.project.gradle.dokka

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.dokka
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { dokka ->
                    plugins.apply(settings.libs.plugins.plugin("dokka").id)

                    dokka.applyTo()
                }
        }
    }
}
