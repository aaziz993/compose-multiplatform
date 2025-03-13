package plugins.animalsniffer

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kmp.KotlinJvmAndAndroidTarget
import gradle.plugins.kmp.jvm.KotlinJvmTarget
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AnimalSnifferPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.animalSniffer
                .takeIf {
                    it.enabled &&
                        projectProperties.kotlin.targets.any { target -> target is KotlinJvmAndAndroidTarget }
                }?.let { animalSniffer ->
                    plugins.apply(settings.libs.plugins.plugin("animalsniffer").id)

                    animalSniffer.applyTo()
                }
        }
    }
}
