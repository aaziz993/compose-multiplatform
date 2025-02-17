package plugin.project.web.js.karakum

import gradle.libs
import org.gradle.api.Project

internal fun Project.configureKarakum() {
    plugins.apply(libs.plugins.karakum.get().pluginId)

    configureKarakumExtension()

    configureKarakumGenerate()
}
