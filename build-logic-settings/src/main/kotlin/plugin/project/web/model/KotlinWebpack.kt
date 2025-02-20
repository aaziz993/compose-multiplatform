package plugin.project.web.model

import kotlinx.serialization.Serializable
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
)
