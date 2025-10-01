package gradle.api.githooks

import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getByType

public val Settings.gitHooks: GitHooksExtension
    get() = extensions.getByType<GitHooksExtension>()
