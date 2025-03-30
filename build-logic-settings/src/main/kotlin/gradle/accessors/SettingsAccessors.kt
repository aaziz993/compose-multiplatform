package gradle.accessors

import gradle.plugins.project.ProjectProperties
import java.util.Properties
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal var Settings.localProperties: Properties
    get() = extraProperties[LOCAL_PROPERTIES_EXT] as Properties
    set(value) {
        extraProperties[LOCAL_PROPERTIES_EXT] = value
    }

internal var Settings.projectProperties: ProjectProperties
    get() = extraProperties[PROJECT_PROPERTIES_EXT] as ProjectProperties
    set(value) {
        extraProperties[PROJECT_PROPERTIES_EXT] = value
    }

internal val Settings.gitHooks
    get() = extensions.getByType<GitHooksExtension>()

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()
