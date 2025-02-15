package plugin.project.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

internal fun Project.configureYarnRootExtension(extension: YarnRootExtension) = extension.apply {
    lockFileDirectory = projectDir

    yarnLockMismatchReport = YarnLockMismatchReport.valueOf(
        providers.gradleProperty("npm.packageLockMismatchReport").get().uppercase(),
    )

    ignoreScripts = providers.gradleProperty("yarn.ignoreScripts").get().toBoolean()
}
