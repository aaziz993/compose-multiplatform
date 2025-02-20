package plugin.project.web.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import net.pearx.kasechange.toScreamingSnakeCase
import net.pearx.kasechange.universalWordSplitter
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import plugin.project.model.target.Target
import plugin.project.model.target.TargetType

@Suppress("MemberVisibilityCanBePrivate")
@kotlinx.serialization.Serializable
internal data class KotlinWebpackConfig(
    val npmProjectDir: String? = null,
    val mode: KotlinWebpackConfig.Mode? = null,
    val entry: String? = null,
    val output: KotlinWebpackOutput? = null,
    val outputPath: String? = null,
    val outputFileName: String? = null,
    val configDirectory: String? = null,
    val reportEvaluatedConfigFile: String? = null,
    val devServer: DevServer? = null,
    val watchOptions: WatchOptions? = null,
    val experiments: Set<String>? = null,
    override val rules: KotlinWebpackRulesContainer,
    val devtool: String? = WebpackDevtool.EVAL_SOURCE_MAP,
    val showProgress: Boolean = false,
    val optimization: Optimization? = null,
    val sourceMaps: Boolean = false,
    val export: Boolean = true,
    val progressReporter: Boolean = false,
    val progressReporterPathFilter: String? = null,
    val resolveFromModulesFirst: Boolean = false
) {

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
//        val proxy: List<Proxy>? = null,
//        val static: List<String>? = null,
//        val contentBase: List<String>? = null,
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
//            val context: List<String>,
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
    @Serializable
    data class WatchOptions(
        val aggregateTimeout: Int? = null,
        val ignored: Boolean? = null
    ) {

        fun toWatchOptions() = KotlinWebpackConfig.WatchOptions(
            aggregateTimeout,
            ignored,
        )
    }

    @Serializable
    data class DevServer(
        val open: Boolean = true,
        val port: Int? = null,
        val proxy: List<Proxy>? = null,
        val static: List<String>? = null,
        val contentBase: List<String>? = null,
        val client: Client? = null
    ) {

        @Serializable
        data class Client(
            @Serializable(with = OverlaySerializer::class)
            val overlay: Any /* Overlay | Boolean */
        ) {

            data class Overlay(
                val errors: Boolean,
                val warnings: Boolean
            ) {

                fun toOverlay() = KotlinWebpackConfig.DevServer.Client.Overlay(
                    errors,
                    warnings,
                )
            }

            fun toClient() = KotlinWebpackConfig.DevServer.Client(
                if (overlay is Overlay) overlay.toOverlay() else overlay,
            )
        }

        private object OverlaySerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

            override fun selectDeserializer(element: JsonElement) = when {
                element is JsonObject -> Client.Overlay::class.serializer()
                else -> Boolean::class.serializer()
            }
        }

        @Serializable
        data class Proxy(
            val context: List<String>,
            val target: String,
            val pathRewrite: MutableMap<String, String>? = null,
            val secure: Boolean? = null,
            val changeOrigin: Boolean? = null
        ) {

            fun toProxy() = KotlinWebpackConfig.DevServer.Proxy(
                context.toMutableList(),
                target,
                pathRewrite,
                secure,
                changeOrigin,
            )
        }

        fun toDevServer() = KotlinWebpackConfig.DevServer(
            open,
            port,
            proxy?.map(Proxy::toProxy)?.toMutableList(),
            static?.toMutableList(),
            contentBase?.toMutableList(),
            client?.toClient(),
        )
    }
}
