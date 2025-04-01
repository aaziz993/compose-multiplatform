package gradle.plugins.kotlin.targets.web

import klib.data.type.reflection.trySet
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
        receiver::useChrome trySet useChrome
        receiver::useChromeHeadless trySet useChromeHeadless
        receiver::useChromeHeadlessNoSandbox trySet useChromeHeadlessNoSandbox
        receiver::useChromium trySet useChromium
        receiver::useChromiumHeadless trySet useChromiumHeadless
        receiver::useChromeCanary trySet useChromeCanary
        receiver::useChromeCanaryHeadless trySet useChromeCanaryHeadless
        receiver::useDebuggableChrome trySet useDebuggableChrome
        receiver::usePhantomJS trySet usePhantomJS
        receiver::useFirefox trySet useFirefox
        receiver::useFirefoxHeadless trySet useFirefoxHeadless
        receiver::useFirefoxDeveloper trySet useFirefoxDeveloper
        receiver::useFirefoxDeveloperHeadless trySet useFirefoxDeveloperHeadless
        receiver::useFirefoxAurora trySet useFirefoxAurora
        receiver::useFirefoxAuroraHeadless trySet useFirefoxAuroraHeadless
        receiver::useFirefoxNightly trySet useFirefoxNightly
        receiver::useFirefoxNightlyHeadless trySet useFirefoxNightlyHeadless
        receiver::useOpera trySet useOpera
        receiver::useSafari trySet useSafari
        receiver::useIe trySet useIe
        receiver::useSourceMapSupport trySet useSourceMapSupport
    }
}
