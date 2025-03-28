package gradle.plugins.shadow

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.shadow.model.ShadowSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ShadowPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.shadow
                .takeIf(ShadowSettings::enabled)?.let { shadow ->
                    plugins.apply(project.settings.libs.plugin("shadow").id)

                    shadow.applyTo()
                }
        }
    }
}
