package plugin.project.web.npm

import gradle.npm
import gradle.projectProperties
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmResolverPlugin

internal fun Project.configureNpmExtension() =
    plugins.withType<NpmResolverPlugin> {
       projectProperties.settings.web.npm.let { npm ->
            npm {
                command tryAssign npm.command
                lockFileName tryAssign npm.lockFileName
                lockFileDirectory tryAssign npm.lockFileDirectory?.let(layout.projectDirectory::dir)
                ignoreScripts tryAssign npm.ignoreScripts
                packageLockMismatchReport tryAssign npm.packageLockMismatchReport
                reportNewPackageLock tryAssign npm.reportNewPackageLock
                packageLockAutoReplace tryAssign npm.packageLockAutoReplace
                overrides tryAssign npm.overrides?.map { override ->
                    org.jetbrains.kotlin.gradle.targets.js.npm.NpmOverride(override.path)
                        .apply {
                            override.includedVersions?.let(includedVersions::addAll)
                            override.excludedVersions?.let(excludedVersions::addAll)
                        }
                }
            }
        }
    }
