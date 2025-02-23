package plugin.project.web.js.karakum

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKarakum() {
    plugins.apply(settings.libs.plugins.plugin("karakum").id)

    configureKarakumExtension()

    configureKarakumGenerate()
}
