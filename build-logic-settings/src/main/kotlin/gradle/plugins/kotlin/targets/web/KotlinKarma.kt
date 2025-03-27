package gradle.plugins.kotlin.targets.web

import kotlinx.serialization.Serializable
import org.gradle.api.Project

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
        useChrome?.takeIfTrue()?.act(receiver::useChrome)
        useChromeHeadless?.takeIfTrue()?.act(receiver::useChromeHeadless)
        useChromeHeadlessNoSandbox?.takeIfTrue()?.act(receiver::useChromeHeadlessNoSandbox)
        useChromium?.takeIfTrue()?.act(receiver::useChromium)
        useChromiumHeadless?.takeIfTrue()?.act(receiver::useChromiumHeadless)
        useChromeCanary?.takeIfTrue()?.act(receiver::useChromeCanary)
        useChromeCanaryHeadless?.takeIfTrue()?.act(receiver::useChromeCanaryHeadless)
        useDebuggableChrome?.takeIfTrue()?.act(receiver::useDebuggableChrome)
        usePhantomJS?.takeIfTrue()?.act(receiver::usePhantomJS)
        useFirefox?.takeIfTrue()?.act(receiver::useFirefox)
        useFirefoxHeadless?.takeIfTrue()?.act(receiver::useFirefoxHeadless)
        useFirefoxDeveloper?.takeIfTrue()?.act(receiver::useFirefoxDeveloper)
        useFirefoxDeveloperHeadless?.takeIfTrue()?.act(receiver::useFirefoxDeveloperHeadless)
        useFirefoxAurora?.takeIfTrue()?.act(receiver::useFirefoxAurora)
        useFirefoxAuroraHeadless?.takeIfTrue()?.act(receiver::useFirefoxAuroraHeadless)
        useFirefoxNightly?.takeIfTrue()?.act(receiver::useFirefoxNightly)
        useFirefoxNightlyHeadless?.takeIfTrue()?.act(receiver::useFirefoxNightlyHeadless)
        useOpera?.takeIfTrue()?.act(receiver::useOpera)
        useSafari?.takeIfTrue()?.act(receiver::useSafari)
        useIe?.takeIfTrue()?.act(receiver::useIe)
        useSourceMapSupport?.takeIfTrue()?.act(receiver::useSourceMapSupport)
    }
}
