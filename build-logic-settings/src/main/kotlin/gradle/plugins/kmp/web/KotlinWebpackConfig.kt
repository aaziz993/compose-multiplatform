package gradle.plugins.kmp.web

import gradle.api.trySet
import gradle.plugins.kmp.web.KotlinWebpackConfig.DevServer.Client.Overlay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@Suppress("MemberVisibilityCanBePrivate")
@Serializable
internal data class KotlinWebpackConfig(
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
    val devtool: String? = null,
    val showProgress: Boolean = false,
    val sourceMaps: Boolean = false,
    val export: Boolean = true,
    val progressReporter: Boolean = false,
    val progressReporterPathFilter: String? = null,
    val resolveFromModulesFirst: Boolean = false,
    val cssSupport: KotlinWebpackCssRule? = null,
    val scssSupport: KotlinWebpackCssRule? = null,
) {

    context(Project)
    fun applyTo(webpackConfig: KotlinWebpackConfig, outputFileName: String) {
        webpackConfig::mode trySet mode
        webpackConfig::entry trySet entry?.let(::file)
        webpackConfig::output trySet output?.toKotlinWebPackOutput()
        webpackConfig::outputPath trySet outputPath?.let(::file)
        webpackConfig.outputFileName = this.outputFileName ?: outputFileName
        webpackConfig::configDirectory trySet configDirectory?.let(::file)
        webpackConfig::reportEvaluatedConfigFile trySet reportEvaluatedConfigFile?.let(::file)

        devServer?.let { devServer ->
            webpackConfig.devServer = (webpackConfig.devServer ?: KotlinWebpackConfig.DevServer())
                .apply(devServer::applyTo)
        }

        watchOptions?.let { watchOptions ->
            webpackConfig.watchOptions = (webpackConfig.watchOptions ?: KotlinWebpackConfig.WatchOptions())
                .apply(watchOptions::applyTo)
        }

        webpackConfig::experiments trySet experiments?.toMutableSet()
        webpackConfig::devtool trySet devtool
        webpackConfig::showProgress trySet showProgress
        webpackConfig::sourceMaps trySet sourceMaps
        webpackConfig::export trySet export
        webpackConfig::progressReporter trySet progressReporter
        webpackConfig::progressReporterPathFilter trySet progressReporterPathFilter?.let(::file)
        webpackConfig::resolveFromModulesFirst trySet resolveFromModulesFirst

        if (cssSupport != null) {
            webpackConfig.cssSupport(cssSupport::applyTo)
        }

        if (scssSupport != null) {
            webpackConfig.scssSupport(scssSupport::applyTo)
        }
    }

    @Serializable
    data class WatchOptions(
        val aggregateTimeout: Int? = null,
        val ignored: Boolean? = null
    ) {

        fun applyTo(receiver: KotlinWebpackConfig.WatchOptions) {
            options::aggregateTimeout trySet aggregateTimeout
            options::ignored trySet ignored
        }
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

            @Serializable
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
                element is JsonObject -> Overlay.serializer()
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

        fun applyTo(receiver: KotlinWebpackConfig.DevServer) {
            server::open trySet open
            server::port trySet port

            proxy?.map(Proxy::toProxy)?.let { proxy ->
                server.proxy = (server.proxy ?: mutableListOf()).apply {
                    addAll(proxy)
                }
            }

            static?.let { static ->
                server.static = (server.static ?: mutableListOf()).apply {
                    addAll(static)
                }
            }

            contentBase?.let { contentBase ->
                server.contentBase = (server.contentBase ?: mutableListOf()).apply {
                    addAll(contentBase)
                }
            }

            client?.toClient()?.let { client ->
                server.client = (server.client ?: KotlinWebpackConfig.DevServer.Client(
                    client.overlay,
                ))
            }
        }
    }
}
