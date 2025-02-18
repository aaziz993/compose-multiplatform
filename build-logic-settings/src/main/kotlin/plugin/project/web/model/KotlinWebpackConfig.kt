package plugin.project.web.model//package plugin.project.web.model
//
//import java.io.File
//import java.io.Serializable
//
//@Suppress("MemberVisibilityCanBePrivate")
//@kotlinx.serialization.Serializable
//internal data class KotlinWebpackConfig(
//    val npmProjectDir: String? = null,
//    val mode: Mode? = null,
//    val entry: String? = null,
//    val output: KotlinWebpackOutput? = null,
//    val outputPath: String? = null,
//    val outputFileName: String? = null,
//    val configDirectory: String? = null,
//    val reportEvaluatedConfigFile: String? = null,
//    val devServer: DevServer? = null,
//    val watchOptions: WatchOptions? = null,
//    val experiments: Set<String> ?=null,
//    override val rules: KotlinWebpackRulesContainer,
//    val devtool: String? = WebpackDevtool.EVAL_SOURCE_MAP,
//    val showProgress: Boolean = false,
//    val optimization: Optimization? = null,
//    val sourceMaps: Boolean = false,
//    val export: Boolean = true,
//    val progressReporter: Boolean = false,
//    val progressReporterPathFilter: String? = null,
//    val resolveFromModulesFirst: Boolean = false
//) {
//
//    enum class Mode(val code: String) {
//        DEVELOPMENT("development"),
//        PRODUCTION("production")
//    }
//
//    @Suppress("unused")
//    @Serializable
//    data class DevServer(
//        val open: Any = true,
//        val port: Int? = null,
//        val proxy: MutableList<Proxy>? = null,
//        val static: MutableList<String>? = null,
//        val contentBase: MutableList<String>? = null,
//        val client: Client? = null
//    ) {
//
//        @Serializable
//        data class Client(
//            val overlay: Any /* Overlay | Boolean */
//        ) {
//
//            data class Overlay(
//                val errors: Boolean,
//                val warnings: Boolean
//            )
//        }
//
//        data class Proxy(
//            val context: MutableList<String>,
//            val target: String,
//            val pathRewrite: MutableMap<String, String>? = null,
//            val secure: Boolean? = null,
//            val changeOrigin: Boolean? = null
//        )
//    }
//
//    @Suppress("unused")
//    data class Optimization(
//        val runtimeChunk: Any?,
//        val splitChunks: Any?
//    )
//
//    @Suppress("unused")
//    @Serializable
//    data class WatchOptions(
//        val aggregateTimeout: Int? = null,
//        val ignored: Any? = null
//    )
//
//    fun save(configFile: File) {
//        configFile.writer().use {
//            appendTo(it)
//        }
//    }
//
//    fun appendTo(target: Appendable) {
//        with(target) {
//            //language=JavaScript 1.8
//            appendLine(
//                    """
//                    let config = {
//                      mode: '${mode.code}',
//                      resolve: {
//                        modules: [
//                          "node_modules"
//                        ]
//                      },
//                      plugins: [],
//                      module: {
//                        rules: []
//                      }
//                    };
//
//                """.trimIndent(),
//            )
//
//            appendEntry()
//            appendResolveModules()
//            appendSourceMaps()
//            appendOptimization()
//            appendDevServer()
//            rules.forEach { rule ->
//                if (rule.active) {
//                    with(rule) { appendToWebpackConfig() }
//                }
//            }
//            appendErrorPlugin()
//            appendFromConfigDir()
//            appendExperiments()
//
//            if (export) {
//                //language=JavaScript 1.8
//                appendLine("module.exports = config")
//            }
//        }
//    }
//}
