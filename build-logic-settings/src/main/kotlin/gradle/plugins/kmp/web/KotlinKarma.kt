package gradle.plugins.kmp.web

import gradle.plugins.buildconfig.generator.BuildConfigJavaGenerator
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer

@Serializable
internal data class KotlinKarma(
    val webpackConfig: KotlinWebpackConfig? = null,
    val useConfigDirectory: String? = null,
    val useChrome: Boolean? = null,
    val useChromeHeadless: Boolean? = null,
    val useChromeHeadlessNoSandbox: Boolean? = null,
    val useChromium: Boolean? = null,
    val useChromiumHeadless: Boolean? = null,
    val useChromeCanary: Boolean? = null,
    val useChromeCanaryHeadless: Boolean? = null,
    val useDebuggableChrome: Boolean? = null,
    val usePhantomJS: Boolean? = null,
    val useFirefox: Boolean? = null,
    val useFirefoxHeadless: Boolean? = null,
    val useFirefoxDeveloper: Boolean? = null,
    val useFirefoxDeveloperHeadless: Boolean? = null,
    val useFirefoxAurora: Boolean? = null,
    val useFirefoxAuroraHeadless: Boolean? = null,
    val useFirefoxNightly: Boolean? = null,
    val useFirefoxNightlyHeadless: Boolean? = null,
    val useOpera: Boolean? = null,
    val useSafari: Boolean? = null,
    val useIe: Boolean? = null,
    val useSourceMapSupport: Boolean? = null,
) {

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.testing.karma.KotlinKarma, outputFileName: String) {
        webpackConfig?.applyTo(receiver.webpackConfig, outputFileName)
        useConfigDirectory?.let(receiver::useConfigDirectory)
        useChrome?.takeIf { it }?.run { receiver.useChrome() }
        useChromeHeadless?.takeIf { it }?.run { receiver.useChromeHeadless() }
        useChromeHeadlessNoSandbox?.takeIf { it }?.run { receiver.useChromeHeadlessNoSandbox() }
        useChromium?.takeIf { it }?.run { receiver.useChromium() }
        useChromiumHeadless?.takeIf { it }?.run { receiver.useChromiumHeadless() }
        useChromeCanary?.takeIf { it }?.run { receiver.useChromeCanary() }
        useChromeCanaryHeadless?.takeIf { it }?.run { receiver.useChromeCanaryHeadless() }
        useDebuggableChrome?.takeIf { it }?.run { receiver.useDebuggableChrome() }
        usePhantomJS?.takeIf { it }?.run { receiver.usePhantomJS() }
        useFirefox?.takeIf { it }?.run { receiver.useFirefox() }
        useFirefoxHeadless?.takeIf { it }?.run { receiver.useFirefoxHeadless() }
        useFirefoxDeveloper?.takeIf { it }?.run { receiver.useFirefoxDeveloper() }
        useFirefoxDeveloperHeadless?.takeIf { it }?.run { receiver.useFirefoxDeveloperHeadless() }
        useFirefoxAurora?.takeIf { it }?.run { receiver.useFirefoxAurora() }
        useFirefoxAuroraHeadless?.takeIf { it }?.run { receiver.useFirefoxAuroraHeadless() }
        useFirefoxNightly?.takeIf { it }?.run { receiver.useFirefoxNightly() }
        useFirefoxNightlyHeadless?.takeIf { it }?.run { receiver.useFirefoxNightlyHeadless() }
        useOpera?.takeIf { it }?.run { receiver.useOpera() }
        useSafari?.takeIf { it }?.run { receiver.useSafari() }
        useIe?.takeIf { it }?.run { receiver.useIe() }
        useSourceMapSupport?.takeIf { it }?.run { receiver.useSourceMapSupport() }
    }
}

internal object KotlinKarmaContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> Boolean.serializer()
            else -> KotlinKarma.serializer()
        }
}
