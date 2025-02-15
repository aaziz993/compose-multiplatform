package plugin.project.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension
import org.gradle.kotlin.dsl.assign

internal fun Project.configureNpmExtension(extension: NpmExtension) = extension.apply {
    lockFileDirectory = projectDir

    packageLockMismatchReport = LockFileMismatchReport.valueOf(
        providers.gradleProperty("npm.packageLockMismatchReport").get().uppercase(),
    )

    ignoreScripts = providers.gradleProperty("npm.ignoreScripts").get().toBoolean()
}
