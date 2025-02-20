package plugin.project.web

import gradle.kotlin
import gradle.moduleProperties
import gradle.tryAssign
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.web.model.BrowserSettings
import plugin.project.web.node.model.NodeSettings

@Suppress("UnstableApiUsage")
internal inline fun <reified T : KotlinJsTargetDsl> Project.configureKotlinJsTarget() =
    moduleProperties.settings.web.let { web ->
        kotlin.targets.withType<T> {
            moduleName = web.moduleName ?: "$group.${project.name}-${targetName}"

            web.browser.takeIf(BrowserSettings::enabled)?.let { browser ->
                browser {
                    val rootDirPath = rootDir.path
                    val projectDirPath = projectDir.path
                    browser.webpackTask?.let { webpack ->
                        webpackTask {
                            ::mode trySet webpack.mode
                            inputFilesDirectory tryAssign webpack.inputFilesDirectory?.let(layout.projectDirectory::dir)
                            entryModuleName tryAssign webpack.entryModuleName
                            esModules tryAssign webpack.esModules
                            webpack.output?.let { output ->
                                this@webpackTask.output::library trySet output.library
                                this@webpackTask.output::libraryTarget trySet output.libraryTarget
                                this@webpackTask.output::globalObject trySet output.globalObject
                            }
                            outputDirectory tryAssign webpack.outputDirectory?.let(layout.projectDirectory::dir)
                            mainOutputFileName tryAssign webpack.mainOutputFileName
                            ::debug trySet webpack.debug
                            ::bin trySet webpack.bin
                            ::args trySet webpack.args?.toMutableList()
                            ::nodeArgs trySet webpack.nodeArgs?.toMutableList()
                            ::sourceMaps trySet webpack.sourceMaps
                            devServerProperty tryAssign webpack.devServerProperty?.toDevServer()
                            ::watchOptions trySet webpack.watchOptions?.toWatchOptions()
                            ::devtool trySet webpack.devtool
                            ::generateConfigOnly trySet webpack.generateConfigOnly
                        }
                    }

                    commonWebpackConfig {
//                        val webpackConfig = KotlinWebpackConfig()

                        npmProjectDir tryAssign webpackConfig.npmProjectDir?.let(::file)
                        ::mode trySet webpackConfig.mode
                        ::entry trySet webpackConfig.entry?.let(::file)
                        output tryAssign webpackConfig.output
                        ::outputPath trySet webpackConfig.outputPath?.let(::file)
                        ::outputFileName trySet webpackConfig.outputFileName
                        ::configDirectory trySet webpackConfig.configDirectory?.let(::file)
                        ::reportEvaluatedConfigFile trySet webpackConfig.reportEvaluatedConfigFile?.let(::file)
                        devServer tryAssign webpackConfig.devServer
                        ::watchOptions trySet webpackConfig.watchOptions
                        ::experiments trySet webpackConfig.experiments
                        ::rules tryAssign webpackConfig.rules
                        ::devtool trySet webpackConfig.devtool
                        ::showProgress trySet webpackConfig.showProgress
                        ::optimization trySet webpackConfig.optimization
                        ::sourceMaps trySet webpackConfig.sourceMaps
                        ::export trySet webpackConfig.export
                        ::progressReporter trySet webpackConfig.progressReporter
                        ::progressReporterPathFilter trySet webpackConfig.progressReporterPathFilter?.let(::file)
                        ::resolveFromModulesFirst trySet webpackConfig.resolveFromModulesFirst

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
