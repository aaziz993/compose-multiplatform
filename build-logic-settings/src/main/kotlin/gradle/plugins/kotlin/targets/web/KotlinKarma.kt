package gradle.plugins.kotlin.targets.web

import gradle.ifTrue
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
        useChrome?.ifTrue(receiver::useChrome)
        useChromeHeadless?.ifTrue(receiver::useChromeHeadless)
        useChromeHeadlessNoSandbox?.ifTrue(receiver::useChromeHeadlessNoSandbox)
        useChromium?.ifTrue(receiver::useChromium)
        useChromiumHeadless?.ifTrue(receiver::useChromiumHeadless)
        useChromeCanary?.ifTrue(receiver::useChromeCanary)
        useChromeCanaryHeadless?.ifTrue(receiver::useChromeCanaryHeadless)
        useDebuggableChrome?.ifTrue(receiver::useDebuggableChrome)
        usePhantomJS?.ifTrue(receiver::usePhantomJS)
        useFirefox?.ifTrue(receiver::useFirefox)
        useFirefoxHeadless?.ifTrue(receiver::useFirefoxHeadless)
        useFirefoxDeveloper?.ifTrue(receiver::useFirefoxDeveloper)
        useFirefoxDeveloperHeadless?.ifTrue(receiver::useFirefoxDeveloperHeadless)
        useFirefoxAurora?.ifTrue(receiver::useFirefoxAurora)
        useFirefoxAuroraHeadless?.ifTrue(receiver::useFirefoxAuroraHeadless)
        useFirefoxNightly?.ifTrue(receiver::useFirefoxNightly)
        useFirefoxNightlyHeadless?.ifTrue(receiver::useFirefoxNightlyHeadless)
        useOpera?.ifTrue(receiver::useOpera)
        useSafari?.ifTrue(receiver::useSafari)
        useIe?.ifTrue(receiver::useIe)
        useSourceMapSupport?.ifTrue(receiver::useSourceMapSupport)
    }
}
