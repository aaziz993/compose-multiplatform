package plugin.project.web

import gradle.isCI
import gradle.maybeNamed
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.assign
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.kmpp.KotlinAmperNamingConvention
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.multiplatform.web.model.JsPlatform
import plugin.multiplatform.web.model.jsPlatform
import plugin.project.KMPEAware

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal open class WebAwarePart(
    ctx: PluginPartCtx,
    targetName: String,
) : KMPEAware, BindingPluginPart by ctx {

    override val kotlinMPE: KotlinMultiplatformExtension =
        project.extensions.getByType(KotlinMultiplatformExtension::class.java)

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        adjustTarget()
        adjustTestTasks()
    }

    private fun adjustTarget() {
//        target.apply {
//            val jsPlatform = project.jsPlatform
//
//            moduleName = "${project.name}${if (targetName == "wasmJs") "-wasm" else ""}"
//
//            if (jsPlatform == JsPlatform.BROWSER || jsPlatform == JsPlatform.WEB) {
//                browser {
//                    val rootDirPath = project.rootDir.path
//                    val projectDirPath = project.projectDir.path
//                    webpackTask {
//                        mainOutputFileName = moduleName
//                    }
//                    commonWebpackConfig {
////                outputFileName = "$moduleName.js"
//                        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                            static = (static ?: mutableListOf()).apply {
//                                // Serve sources to debug inside browser
//                                add(rootDirPath)
//                                add(projectDirPath)
//                            }
//                        }
//                        cssSupport {
//                            enabled = true
//                        }
//                    }
//                }
//            }
//
//            if (jsPlatform == JsPlatform.NODE || jsPlatform == JsPlatform.WEB) {
//                nodejs()
//            }
//
////    binaries.executable()
//        }
    }

    internal fun adjustTestTasks() = with(project) {
        val shouldRunJsBrowserTest = !isCI || hasProperty("enable-js-tests")
        if (shouldRunJsBrowserTest) return

//        tasks.maybeNamed("clean${target.targetName.capitalized()}BrowserTest") { onlyIf { false } }
//        tasks.maybeNamed("${target.targetName.capitalized()}BrowserTest") { onlyIf { false } }
    }
}
