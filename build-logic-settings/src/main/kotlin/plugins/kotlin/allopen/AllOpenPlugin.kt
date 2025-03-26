package plugins.kotlin.allopen

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.kotlin.allopen.model.AllOpenSettings

internal class AllOpenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.allOpen
                .takeIf(AllOpenSettings::enabled)?.let { allOpen ->
                    plugins.apply(project.settings.libs.plugins.plugin("allopen").id)

                    allOpen.applyTo()
                }
        }
    }
}
