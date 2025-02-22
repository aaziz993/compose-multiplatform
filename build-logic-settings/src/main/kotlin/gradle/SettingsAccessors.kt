package gradle

import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.Properties

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

private const val PROPERTIES = "properties"

internal var Settings.projectProperties: Properties

    get() = extraProperties[PROPERTIES] as Properties
    set(value) {
        extraProperties[PROPERTIES] = value
    }
