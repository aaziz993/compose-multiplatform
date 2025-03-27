package gradle.plugins.kotlin.targets.web

import gradle.actIfTrue
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
        useChrome?.actIfTrue(receiver::useChrome)
        useChromeHeadless?.actIfTrue(receiver::useChromeHeadless)
        useChromeHeadlessNoSandbox?.actIfTrue(receiver::useChromeHeadlessNoSandbox)
        useChromium?.actIfTrue(receiver::useChromium)
        useChromiumHeadless?.actIfTrue(receiver::useChromiumHeadless)
        useChromeCanary?.actIfTrue(receiver::useChromeCanary)
        useChromeCanaryHeadless?.actIfTrue(receiver::useChromeCanaryHeadless)
        useDebuggableChrome?.actIfTrue(receiver::useDebuggableChrome)
        usePhantomJS?.actIfTrue(receiver::usePhantomJS)
        useFirefox?.actIfTrue(receiver::useFirefox)
        useFirefoxHeadless?.actIfTrue(receiver::useFirefoxHeadless)
        useFirefoxDeveloper?.actIfTrue(receiver::useFirefoxDeveloper)
        useFirefoxDeveloperHeadless?.actIfTrue(receiver::useFirefoxDeveloperHeadless)
        useFirefoxAurora?.actIfTrue(receiver::useFirefoxAurora)
        useFirefoxAuroraHeadless?.actIfTrue(receiver::useFirefoxAuroraHeadless)
        useFirefoxNightly?.actIfTrue(receiver::useFirefoxNightly)
        useFirefoxNightlyHeadless?.actIfTrue(receiver::useFirefoxNightlyHeadless)
        useOpera?.actIfTrue(receiver::useOpera)
        useSafari?.actIfTrue(receiver::useSafari)
        useIe?.actIfTrue(receiver::useIe)
        useSourceMapSupport?.actIfTrue(receiver::useSourceMapSupport)
    }
}
