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
    fun applyTo(recipient: org.jetbrains.kotlin.gradle.targets.js.testing.karma.KotlinKarma, outputFileName: String) {
        webpackConfig?.applyTo(recipient.webpackConfig, outputFileName)
        useConfigDirectory?.let(recipient::useConfigDirectory)
        useChrome?.takeIf { it }?.run { recipient.useChrome() }
        useChromeHeadless?.takeIf { it }?.run { recipient.useChromeHeadless() }
        useChromeHeadlessNoSandbox?.takeIf { it }?.run { recipient.useChromeHeadlessNoSandbox() }
        useChromium?.takeIf { it }?.run { recipient.useChromium() }
        useChromiumHeadless?.takeIf { it }?.run { recipient.useChromiumHeadless() }
        useChromeCanary?.takeIf { it }?.run { recipient.useChromeCanary() }
        useChromeCanaryHeadless?.takeIf { it }?.run { recipient.useChromeCanaryHeadless() }
        useDebuggableChrome?.takeIf { it }?.run { recipient.useDebuggableChrome() }
        usePhantomJS?.takeIf { it }?.run { recipient.usePhantomJS() }
        useFirefox?.takeIf { it }?.run { recipient.useFirefox() }
        useFirefoxHeadless?.takeIf { it }?.run { recipient.useFirefoxHeadless() }
        useFirefoxDeveloper?.takeIf { it }?.run { recipient.useFirefoxDeveloper() }
        useFirefoxDeveloperHeadless?.takeIf { it }?.run { recipient.useFirefoxDeveloperHeadless() }
        useFirefoxAurora?.takeIf { it }?.run { recipient.useFirefoxAurora() }
        useFirefoxAuroraHeadless?.takeIf { it }?.run { recipient.useFirefoxAuroraHeadless() }
        useFirefoxNightly?.takeIf { it }?.run { recipient.useFirefoxNightly() }
        useFirefoxNightlyHeadless?.takeIf { it }?.run { recipient.useFirefoxNightlyHeadless() }
        useOpera?.takeIf { it }?.run { recipient.useOpera() }
        useSafari?.takeIf { it }?.run { recipient.useSafari() }
        useIe?.takeIf { it }?.run { recipient.useIe() }
        useSourceMapSupport?.takeIf { it }?.run { recipient.useSourceMapSupport() }
    }
}

internal object KotlinKarmaSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> Boolean.serializer()
            else -> KotlinKarma.serializer()
        }
}
