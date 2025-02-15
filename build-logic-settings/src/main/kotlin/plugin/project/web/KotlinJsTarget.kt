package plugin.project.web

import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.amper.gradle.amperModule
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import gradle.amperModuleExtraProperties
import plugin.project.web.model.JsPlatform
import plugin.project.web.model.jsPlatform

internal fun Project.configureKotlinJsTarget(target: KotlinJsTargetDsl) =
    amperModuleExtraProperties.settings?.web.let { web ->
        target.apply {
            val module = amperModule
            val jsPlatform = jsPlatform
            val isApp = amperModuleExtraProperties.product.webApp

           moduleNametrySet  web?.framework?.moduleName ?: module?.userReadableName

            if (jsPlatform == JsPlatform.BROWSER || jsPlatform == JsPlatform.WEB) {
                browser {
                    val rootDirPath = rootDir.path
                    val projectDirPath = projectDir.path
                    webpackTask {
                        mainOutputFileName = moduleName
                    }
                    commonWebpackConfig {
                        if (isApp) {
                            outputFileName = "$moduleName.js"
                        }
                        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                            static = (static ?: mutableListOf()).apply {
                                // Serve sources to debug inside browser
                                add(rootDirPath)
                                add(projectDirPath)
                            }
                        }
                        cssSupport {
                            enabled = true
                        }
                    }
                }
            }

            if (jsPlatform == JsPlatform.NODE || jsPlatform == JsPlatform.WEB) {
                nodejs()
            }

            if (isApp) {
                binaries.executable()
            }
            else {
                binaries.library()
            }
        }
    }
