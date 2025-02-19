package plugin.project.web.yarn

import gradle.moduleProperties
import gradle.trySet
import gradle.yarn
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnResolution

internal fun Project.configureYarnRootExtension() =
    plugins.withType<YarnPlugin> {
        moduleProperties.settings.web.yarn.let { yarn ->
            yarn {
                ::lockFileName trySet yarn.lockFileName
                ::lockFileDirectory trySet yarn.lockFileDirectory?.let(::file)
                ::ignoreScripts trySet yarn.ignoreScripts
                ::yarnLockMismatchReport trySet yarn.yarnLockMismatchReport
                ::reportNewYarnLock trySet yarn.reportNewYarnLock
                ::yarnLockAutoReplace trySet yarn.yarnLockAutoReplace
                ::resolutions trySet yarn.resolutions?.map { resolution ->
                    YarnResolution(resolution.path).apply {
                        ::includedVersions trySet resolution.includedVersions?.toMutableList()
                        ::excludedVersions trySet resolution.excludedVersions?.toMutableList()
                    }
                }?.toMutableList()

                lockFileDirectory = projectDir

                yarnLockMismatchReport = YarnLockMismatchReport.valueOf(
                    providers.gradleProperty("npm.packageLockMismatchReport").get().uppercase(),
                )

                ignoreScripts = providers.gradleProperty("yarn.ignoreScripts").get().toBoolean()
            }
        }
    }
