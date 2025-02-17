package gradle

import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.settings.model.ProjectProperties

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

private const val PROJECT_EXTRA_PROPERTIES = "org.jetbrains.amper.gradle.ext.projectExtraProperties"

internal var Settings.amperProjectExtraProperties: ProjectProperties
    get() = extraProperties[PROJECT_EXTRA_PROPERTIES] as ProjectProperties
    set(value) {
        extraProperties[PROJECT_EXTRA_PROPERTIES] = value
    }
