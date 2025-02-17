package plugin.project.web

import gradle.amperModuleExtraProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.web.model.BrowserSettings
import plugin.project.web.node.configureNodeJsRootExtension
import plugin.project.web.node.model.NodeSettings

internal fun Project.configureKotlinJsTarget(target: KotlinJsTargetDsl) =
    amperModuleExtraProperties.settings.web.let { web ->
        target.apply {
            moduleName = web.moduleName ?: "$group.${project.name}-${target.name}"

            web.browser.takeIf(BrowserSettings::enabled)?.let { browser ->
                println("Configure $targetName browser")

                browser {
                    val rootDirPath = rootDir.path
                    val projectDirPath = projectDir.path
                    webpackTask {
                        mainOutputFileName = moduleName
                    }
                    commonWebpackConfig {

                        outputFileName = "$moduleName.js"

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
            web.node.takeIf(NodeSettings::enabled)?.let { node ->
                nodejs()
            }

            if (web.executable) {
                binaries.executable()
            }
            else {
                binaries.library()
            }
        }
    }
