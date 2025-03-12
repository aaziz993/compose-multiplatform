package gradle

import gradle.plugins.project.ProjectProperties
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()

private const val PROPERTIES = "settings.project.properties"

internal var Settings.projectProperties: ProjectProperties
    get() = extraProperties[PROPERTIES] as ProjectProperties
    set(value) {
        extraProperties[PROPERTIES] = value
    }

internal val Settings.gitHooks
    get() = extensions.getByType<GitHooksExtension>()
