package gradle.plugins.animalsniffer

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.animalsniffer.model.AnimalSnifferSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.animalSniffer
                .takeIf(AnimalSnifferSettings::enabled)?.let { animalSniffer ->
                    plugins.apply(project.settings.libs.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
