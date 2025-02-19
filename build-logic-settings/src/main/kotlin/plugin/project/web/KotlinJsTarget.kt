package plugin.project.web

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.web.model.BrowserSettings
import plugin.project.web.node.model.NodeSettings

internal inline fun <reified T : KotlinJsTargetDsl> Project.configureKotlinJsTarget() =
    moduleProperties.settings.web.let { web ->
        kotlin.targets.withType<T> {
            moduleName = web.moduleName ?: "$group.${project.name}-${targetName}"

            web.browser.takeIf(BrowserSettings::enabled)?.let { browser ->
                println("Configure $targetName browser")

                browser {
                    val rootDirPath = rootDir.path
                    val projectDirPath = projectDir.path
                    webpackTask {
                        mainOutputFileName = moduleName
                    }
                    commonWebpackConfig {
//                        val webpackConfig = KotlinWebpackConfig()

//                        npmProjectDir tryAssign webpackConfig.npmProjectDir?.let(::file)
//                        ::mode trySet webpackConfig.mode
//                        ::entry trySet webpackConfig.entry?.let(::file)
//                        output tryAssign webpackConfig.output
//                        ::outputPath trySet webpackConfig.outputPath?.let(::file)
//                        ::outputFileName trySet webpackConfig.outputFileName
//                        ::configDirectory trySet webpackConfig.configDirectory?.let(::file)
//                        ::reportEvaluatedConfigFile trySet webpackConfig.reportEvaluatedConfigFile?.let(::file)
//                        devServer tryAssign webpackConfig.devServer
//                        ::watchOptions trySet webpackConfig.watchOptions
//                        ::experiments trySet webpackConfig.experiments
//                        ::rules tryAssign webpackConfig.rules
//                        ::devtool trySet webpackConfig.devtool
//                        ::showProgress trySet webpackConfig.showProgress
//                        ::optimization trySet webpackConfig.optimization
//                        ::sourceMaps trySet webpackConfig.sourceMaps
//                        ::export trySet webpackConfig.export
//                        ::progressReporter trySet webpackConfig.progressReporter
//                        ::progressReporterPathFilter trySet webpackConfig.progressReporterPathFilter?.let(::file)
//                        ::resolveFromModulesFirst trySet webpackConfig.resolveFromModulesFirst

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
