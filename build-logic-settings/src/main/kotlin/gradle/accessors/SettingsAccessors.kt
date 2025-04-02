package gradle.accessors

import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType

internal val Settings.gitHooks
    get() = extensions.getByType<GitHooksExtension>()

internal fun Settings.execute(cmd: String): String = providers.exec {
    commandLine(cmd.split(" "))
}.standardOutput.asText.get().trim()
