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
import org.jetbrains.kotlin.gradle.targets.js.NpmVersions
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.web.model.BrowserSettings
import plugin.project.web.model.KotlinWebpackOutput
import plugin.project.web.node.model.NodeSettings

@Suppress("UnstableApiUsage")
internal inline fun <reified T : KotlinJsTargetDsl> Project.configureKotlinJsTarget() =
    moduleProperties.settings.web.let { web ->
        kotlin.targets.withType<T> {
            moduleName = web.moduleName ?: "$group.${project.name}-${targetName}"

            web.browser.takeIf(BrowserSettings::enabled)?.let { browser ->
                browser {
                    browser.webpackTask?.let { webpack ->
                        webpackTask {
                            ::mode trySet webpack.mode
                            inputFilesDirectory tryAssign webpack.inputFilesDirectory?.let(layout.projectDirectory::dir)
                            entryModuleName tryAssign webpack.entryModuleName
                            esModules tryAssign webpack.esModules
                            webpack.output?.let(output::configureFrom)
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

                    browser.commonWebpackConfig?.let { commonWebpackConfig ->
                        commonWebpackConfig {
                            ::mode trySet commonWebpackConfig.mode
                            ::entry trySet commonWebpackConfig.entry?.let(::file)
                            commonWebpackConfig.output?.let {
                                output?.configureFrom(it)
                            }
                            ::outputPath trySet commonWebpackConfig.outputPath?.let(::file)
                            ::outputFileName trySet commonWebpackConfig.outputFileName
                            ::configDirectory trySet commonWebpackConfig.configDirectory?.let(::file)
                            ::reportEvaluatedConfigFile trySet commonWebpackConfig.reportEvaluatedConfigFile?.let(::file)
                            ::devServer trySet commonWebpackConfig.devServer?.toDevServer()
                            ::watchOptions trySet commonWebpackConfig.watchOptions?.toWatchOptions()
                            ::experiments trySet commonWebpackConfig.experiments?.toMutableSet()
                            ::devtool trySet commonWebpackConfig.devtool
                            ::showProgress trySet commonWebpackConfig.showProgress
                            ::optimization trySet commonWebpackConfig.optimization
                            ::sourceMaps trySet commonWebpackConfig.sourceMaps
                            ::export trySet commonWebpackConfig.export
                            ::progressReporter trySet commonWebpackConfig.progressReporter
                            ::progressReporterPathFilter trySet commonWebpackConfig.progressReporterPathFilter?.let(::file)
                            ::resolveFromModulesFirst trySet commonWebpackConfig.resolveFromModulesFirst
                            commonWebpackConfig.cssSupport?.let { cssSupport ->
                                cssSupport {
                                    enabled tryAssign cssSupport.enabled
                                    test tryAssign cssSupport.test
                                    include tryAssign cssSupport.include
                                    exclude tryAssign cssSupport.exclude
                                    cssSupport.validate?.run { validate() }
//
                                }
                            }
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

private fun org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.configureFrom(
    config: KotlinWebpackOutput
) {
    ::library trySet config.library
    ::libraryTarget trySet config.libraryTarget
    ::globalObject trySet config.globalObject
}
