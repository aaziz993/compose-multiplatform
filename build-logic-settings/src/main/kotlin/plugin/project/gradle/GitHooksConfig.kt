package plugin.project.gradle

import gradle.versionCatalog
import org.danilopianini.gradle.git.hooks.GitHooksExtension
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure

internal fun Settings.configureGitHooks() {
    apply(versionCatalog.getTable("plugins")!!.getTable("gradle-pre-commit-git-hooks")!!.getString("id")!!)

    extensions.configure<GitHooksExtension>(::configureGitHooksExtension)
}

private fun Settings.configureGitHooksExtension(extension: GitHooksExtension) =
    extension.apply {

    }
