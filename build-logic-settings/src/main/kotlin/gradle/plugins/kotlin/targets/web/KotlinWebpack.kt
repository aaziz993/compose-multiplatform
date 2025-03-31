package gradle.plugins.kotlin.targets.web

import gradle.api.provider.tryAssign
import gradle.collection.tryAddAll
import gradle.plugins.kotlin.targets.web.KotlinWebpackConfig.WatchOptions
import gradle.reflect.trySet
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
        receiver::mode trySet mode
        receiver.inputFilesDirectory tryAssign inputFilesDirectory?.let(project.layout.projectDirectory::dir)
        receiver.entryModuleName tryAssign entryModuleName
        receiver.esModules tryAssign esModules
        output?.applyTo(receiver.output)
        receiver.outputDirectory tryAssign outputDirectory?.let(project.layout.projectDirectory::dir)
        receiver.mainOutputFileName tryAssign mainOutputFileName
        receiver::debug trySet debug
        receiver::bin trySet bin
        receiver.args tryAddAll args
        receiver.nodeArgs tryAddAll nodeArgs
        receiver::sourceMaps trySet sourceMaps
        receiver.devServerProperty tryAssign devServerProperty?.toDevServer()
        receiver::watchOptions trySet watchOptions?.toWatchOptions()

        receiver::watchOptions.trySet(
            watchOptions,
            WatchOptions::toWatchOptions,
            WatchOptions::applyTo,
        )

        receiver::devtool trySet devtool
        receiver::generateConfigOnly trySet generateConfigOnly
    }
}
