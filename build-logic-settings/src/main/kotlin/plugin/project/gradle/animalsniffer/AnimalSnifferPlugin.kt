package plugin.project.gradle.animalsniffer

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.animalSniffer
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty() == true }?.let { animalSniffer ->
                    plugins.apply(settings.libs.plugins.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
