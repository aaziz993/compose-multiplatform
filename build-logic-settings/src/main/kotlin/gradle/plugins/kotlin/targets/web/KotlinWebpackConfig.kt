package gradle.plugins.kotlin.targets.web

import gradle.api.tryAddAll
import gradle.api.tryApply
import gradle.api.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
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
    val setDevServer: DevServer? = null,
    val watchOptions: WatchOptions? = null,
    val setWatchOptions: WatchOptions? = null,
    val experiments: Set<String>? = null,
    val setExperiments: Set<String>? = null,
    val devtool: String? = null,
    val showProgress: Boolean = false,
    val sourceMaps: Boolean = false,
    val export: Boolean = true,
    val progressReporter: Boolean = false,
    val resolveFromModulesFirst: Boolean = false,
    val cssSupport: KotlinWebpackCssRule? = null,
    val scssSupport: KotlinWebpackCssRule? = null,
) {

    context(Project)
    fun applyTo(webpackConfig: KotlinWebpackConfig, outputFileName: String) {
        webpackConfig::mode trySet mode
        webpackConfig::entry trySet entry?.let(project::file)
        webpackConfig::output trySet output?.toKotlinWebPackOutput()
        webpackConfig::outputPath trySet outputPath?.let(project::file)
        webpackConfig.outputFileName = this.outputFileName ?: outputFileName
        webpackConfig::configDirectory trySet configDirectory?.let(project::file)
        webpackConfig::reportEvaluatedConfigFile trySet reportEvaluatedConfigFile?.let(project::file)

        webpackConfig::devServer.trySet(
            devServer,
            DevServer::toDevServer,
            DevServer::applyTo,
        )

        webpackConfig::watchOptions trySet watchOptions?.toWatchOptions()

        webpackConfig::watchOptions.trySet(
            watchOptions,
            WatchOptions::toWatchOptions,
            WatchOptions::applyTo,
        )

        webpackConfig::watchOptions trySet setWatchOptions?.toWatchOptions()
        experiments?.let(webpackConfig.experiments::addAll)
        webpackConfig::experiments trySet setExperiments?.toMutableSet()
        webpackConfig::devtool trySet devtool
        webpackConfig::showProgress trySet showProgress
        webpackConfig::sourceMaps trySet sourceMaps
        webpackConfig::export trySet export
        webpackConfig::progressReporter trySet progressReporter
        webpackConfig::resolveFromModulesFirst trySet resolveFromModulesFirst
        webpackConfig::cssSupport tryApply cssSupport?.let { it::applyTo }
        webpackConfig::scssSupport tryApply scssSupport?.let { it::applyTo }
    }

    @Serializable
    data class WatchOptions(
        val aggregateTimeout: Int? = null,
        val ignored: Boolean? = null
    ) {

        fun toWatchOptions() = KotlinWebpackConfig.WatchOptions(aggregateTimeout, ignored)

        fun applyTo(receiver: KotlinWebpackConfig.WatchOptions) {
            receiver::aggregateTimeout trySet aggregateTimeout
            receiver::ignored trySet ignored
        }
    }

    @Serializable
    data class DevServer(
        val open: Boolean = true,
        val port: Int? = null,
        val proxy: List<Proxy>? = null,
        val setProxy: List<Proxy>? = null,
        val static: List<String>? = null,
        val setStatic: List<String>? = null,
        val contentBase: List<String>? = null,
        val setContentBase: List<String>? = null,
        val client: Client? = null,
        val setClient: Client? = null
    ) {

        @Serializable
        data class Client(
            @Serializable(with = OverlayContentPolymorphicSerializer::class)
            @SerialName("overlay")
            private val _overlay: Any /* Overlay | Boolean */
        ) {

            val overlay by lazy {
                if (_overlay is Overlay) _overlay.toOverlay() else _overlay
            }

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
                overlay,
            )

            fun applyTo(receiver: KotlinWebpackConfig.DevServer.Client) {
                receiver::overlay trySet overlay
            }
        }

        private object OverlayContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

            override fun selectDeserializer(element: JsonElement) =
                if (element is JsonPrimitive) Boolean::class.serializer()
                else Client.Overlay.serializer()
        }

        @Serializable
        data class Proxy(
            val context: List<String>,
            val target: String,
            val pathRewrite: Map<String, String>? = null,
            val secure: Boolean? = null,
            val changeOrigin: Boolean? = null
        ) {

            fun toProxy() = KotlinWebpackConfig.DevServer.Proxy(
                context.toMutableList(),
                target,
                pathRewrite?.toMutableMap(),
                secure,
                changeOrigin,
            )
        }

        fun toDevServer() =
            org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer(
                open,
                port,
                proxy?.map(Proxy::toProxy)?.toMutableList(),
                static?.toMutableList(),
                contentBase?.toMutableList(),
                client?.toClient(),
            )

        fun applyTo(receiver: KotlinWebpackConfig.DevServer) {
            receiver::open trySet open
            receiver::port trySet port
            receiver::proxy tryAddAll proxy?.map(Proxy::toProxy)
            receiver::proxy trySet setProxy?.map(Proxy::toProxy)?.toMutableList()
            receiver::static tryAddAll static
            receiver::static trySet setStatic?.toMutableList()
            receiver::contentBase tryAddAll contentBase
            receiver::contentBase trySet setContentBase?.toMutableList()

            receiver::client.trySet(
                client,
                Client::toClient,
                Client::applyTo,
            )

            receiver::client trySet client?.toClient()
        }
    }
}


