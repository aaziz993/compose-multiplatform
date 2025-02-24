package gradle

import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import plugin.project.model.Properties

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

private const val PROPERTIES = "settings.project.properties"

internal var Settings.projectProperties: Properties
    get() = extraProperties[PROPERTIES] as Properties
    set(value) {
        extraProperties[PROPERTIES] = value
    }

internal val Settings.gitHooks
    get() = extensions.getByType<GitHooksExtension>()
