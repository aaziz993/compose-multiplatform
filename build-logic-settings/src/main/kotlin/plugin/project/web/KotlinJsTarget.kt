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
import org.jetbrains.kotlin.gradle.dsl.JsMainFunctionExecutionMode
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.JsSourceMapEmbedMode
import org.jetbrains.kotlin.gradle.dsl.JsSourceMapNamesPolicy
import org.jetbrains.kotlin.gradle.targets.js.NpmVersions
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.kotlin.model.language.configureFrom
import plugin.project.web.model.BrowserSettings
import plugin.project.web.model.KotlinJsCompilerOptions
import plugin.project.web.model.KotlinWebpackCssRule
import plugin.project.web.model.KotlinWebpackOutput
import plugin.project.web.model.KotlinWebpackRule
import plugin.project.web.node.model.NodeSettings

@OptIn(ExperimentalMainFunctionArgumentsDsl::class)
@Suppress("UnstableApiUsage")
internal inline fun <reified T : KotlinJsTargetDsl> Project.configureKotlinJsTarget() =
    moduleProperties.settings.web.let { web ->
        kotlin.targets.withType<T> {
            ::moduleName trySet web.moduleName
            web.useCommonJs?.takeIf { it }?.run { useCommonJs() }
            web.useEsModules?.takeIf { it }?.run { useEsModules() }
            web.passAsArgumentToMainFunction?.let(::passAsArgumentToMainFunction)
            web.generateTypeScriptDefinitions?.takeIf { it }?.let { generateTypeScriptDefinitions() }

            web.compilerOptions?.let { compilerOptions ->
                compilerOptions {
                    configureFrom(compilerOptions)
                    friendModulesDisabled tryAssign compilerOptions.friendModulesDisabled
                    main tryAssign compilerOptions.main
                    moduleKind tryAssign compilerOptions.moduleKind
                    moduleName tryAssign compilerOptions.moduleName
                    sourceMap tryAssign compilerOptions.sourceMap
                    sourceMapEmbedSources tryAssign compilerOptions.sourceMapEmbedSources
                    sourceMapNamesPolicy tryAssign compilerOptions.sourceMapNamesPolicy
                    sourceMapPrefix tryAssign compilerOptions.sourceMapPrefix
                    target tryAssign compilerOptions.target
                    useEsClasses tryAssign compilerOptions.useEsClasses
                }
            }

            web.browser.takeIf(BrowserSettings::enabled)?.let { browser ->
                browser {
                    testTask {
                         targetName=""
                         filter.includeTestsMatching()
                         filter.excludeTestsMatching()
                         filter.setExcludePatterns()
                         filter.setExcludePatterns()
                        filter.setIncludePatterns()
                        setTestNameIncludePatterns()
                        ignoreFailures
                        filter.setCommandLineIncludePatterns()
                        filter.isFailOnNoMatchingTests=true
                         ignoreRunFailures: Boolean? = null,
                    }

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
                            webpack.args?.let(args::addAll)
                            webpack.nodeArgs?.let(nodeArgs::addAll)
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
                            ::sourceMaps trySet commonWebpackConfig.sourceMaps
                            ::export trySet commonWebpackConfig.export
                            ::progressReporter trySet commonWebpackConfig.progressReporter
                            ::progressReporterPathFilter trySet commonWebpackConfig.progressReporterPathFilter?.let(::file)
                            ::resolveFromModulesFirst trySet commonWebpackConfig.resolveFromModulesFirst
                            commonWebpackConfig.cssSupport?.let { cssSupport ->
                                cssSupport {
                                    configureFrom(cssSupport)
                                }
                            }
                            commonWebpackConfig.cssSupport?.let { cssSupport ->
                                scssSupport {
                                    configureFrom(cssSupport)
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

private fun org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackCssRule.configureFrom(
    config: KotlinWebpackCssRule
) {
    mode tryAssign config.mode
    enabled tryAssign config.enabled
    test tryAssign config.test
    include tryAssign config.include
    exclude tryAssign config.exclude
    config.validate?.takeIf { it }.run { validate() }
}
