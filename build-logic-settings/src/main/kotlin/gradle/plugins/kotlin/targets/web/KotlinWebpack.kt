package gradle.plugins.kotlin.targets.web

import gradle.api.tryAddAll
import gradle.api.tryAssign
import gradle.api.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

@Serializable
internal data class KotlinWebpack(
    val mode: Mode? = null,
    val inputFilesDirectory: String? = null,
    val entryModuleName: String? = null,
    val esModules: Boolean? = null,
    val output: KotlinWebpackOutput? = null,
    val outputDirectory: String? = null,
    val mainOutputFileName: String? = null,
    val debug: Boolean? = null,
    val bin: String? = null,
    val args: List<String>? = null,
    val nodeArgs: List<String>? = null,
    val sourceMaps: Boolean? = null,
    val devServerProperty: KotlinWebpackConfig.DevServer? = null,
    val watchOptions: KotlinWebpackConfig.WatchOptions? = null,
    val devtool: String? = null,
    val generateConfigOnly: Boolean? = null,
) {

    context(Project)
    @Suppress("UnstableApiUsage")
    fun applyTo(receiver: KotlinWebpack) {
        webpack::mode trySet mode
        webpack.inputFilesDirectory tryAssign inputFilesDirectory?.let(project.layout.projectDirectory::dir)
        webpack.entryModuleName tryAssign entryModuleName
        webpack.esModules tryAssign esModules
        output?.applyTo(webpack.output)
        webpack.outputDirectory tryAssign outputDirectory?.let(project.layout.projectDirectory::dir)
        webpack.mainOutputFileName tryAssign mainOutputFileName
        webpack::debug trySet debug
        webpack::bin trySet bin
        webpack.args tryAddAll args
        webpack.nodeArgs tryAddAll nodeArgs
        webpack::sourceMaps trySet sourceMaps
        webpack.devServerProperty tryAssign devServerProperty?.toDevServer()

        watchOptions?.let { watchOptions ->
            webpack.watchOptions = (webpack.watchOptions
                ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.WatchOptions())
                .apply(watchOptions::applyTo)
        }

        webpack::devtool trySet devtool
        webpack::generateConfigOnly trySet generateConfigOnly
    }
}
