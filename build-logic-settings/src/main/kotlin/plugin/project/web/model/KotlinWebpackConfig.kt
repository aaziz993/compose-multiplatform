//package plugin.project.web.model
//
//import kotlinx.serialization.Serializable
//import org.gradle.api.provider.Provider
//import org.jetbrains.kotlin.gradle.targets.js.NpmVersions
//import org.jetbrains.kotlin.gradle.targets.js.RequiredKotlinJsDependency
//import org.jetbrains.kotlin.gradle.targets.js.appendConfigsFromDir
//import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWebpackRulesContainer
//import org.jetbrains.kotlin.gradle.targets.js.dsl.WebpackRulesDsl
//import org.jetbrains.kotlin.gradle.targets.js.jsQuoted
//import org.jetbrains.kotlin.gradle.utils.appendLine
//import org.jetbrains.kotlin.gradle.utils.relativeOrAbsolute
//import java.io.File
//import java.io.StringWriter
//import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
//
//@Serializable
//internal data class KotlinWebpackConfig(]
//    var mode: KotlinWebpackConfig.Mode? = null,
//    var entry: Str? = null,
//    var output: KotlinWebpackOutput? = null,
//    var outputPath: File? = null,
//    var outputFileName: String? = entry?.name,
//    var configDirectory: File? = null,
//    var reportEvaluatedConfigFile: File? = null,
//    var devServer: DevServer? = null,
//    var watchOptions: WatchOptions? = null,
//    var experiments: MutableSet<String> = mutableSetOf(),
//    override val rules: KotlinWebpackRulesContainer,
//    var devtool: String? = WebpackDevtool.EVAL_SOURCE_MAP,
//    var showProgress: Boolean = false,
//    var optimization: Optimization? = null,
//    var sourceMaps: Boolean = false,
//    var export: Boolean = true,
//    var progressReporter: Boolean = false,
//    var progressReporterPathFilter: File? = null,
//    var resolveFromModulesFirst: Boolean = false
//)
