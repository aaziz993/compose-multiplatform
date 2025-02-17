package plugin.project.web.npm

import gradle.amperModuleExtraProperties
import gradle.npm
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmResolverPlugin

internal fun Project.configureNpmExtension() =
    plugins.withType<NpmResolverPlugin> {
        amperModuleExtraProperties.settings.web.npm.let { npm ->
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
                            ::includedVersions trySet override.includedVersions?.toMutableList()
                            ::excludedVersions trySet override.excludedVersions?.toMutableList()
                        }
                }
            }
        }
    }
