package gradle.plugins.animalsniffer

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.animalsniffer.model.AnimalSnifferSettings
import gradle.plugins.kmp.KotlinJvmAndAndroidTarget
import gradle.plugins.kmp.android.KotlinAndroidTarget
import gradle.plugins.kmp.jvm.KotlinJvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.animalSniffer
                .takeIf(AnimalSnifferSettings::enabled)?.let { animalSniffer ->
                    plugins.apply(project.settings.libs.plugins.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
