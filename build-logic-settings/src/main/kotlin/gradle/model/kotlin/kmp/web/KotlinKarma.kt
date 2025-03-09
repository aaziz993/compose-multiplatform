package gradle.model.kotlin.kmp.web

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.testing.karma.KotlinKarma

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
    fun applyTo(karma: KotlinKarma, moduleName: String) {
        webpackConfig?.applyTo(karma.webpackConfig, moduleName)

        useConfigDirectory?.let(karma::useConfigDirectory)
        useChrome?.takeIf { it }?.run { karma.useChrome() }
        useChromeHeadless?.takeIf { it }?.run { karma.useChromeHeadless() }
        useChromeHeadlessNoSandbox?.takeIf { it }?.run { karma.useChromeHeadlessNoSandbox() }
        useChromium?.takeIf { it }?.run { karma.useChromium() }
        useChromiumHeadless?.takeIf { it }?.run { karma.useChromiumHeadless() }
        useChromeCanary?.takeIf { it }?.run { karma.useChromeCanary() }
        useChromeCanaryHeadless?.takeIf { it }?.run { karma.useChromeCanaryHeadless() }
        useDebuggableChrome?.takeIf { it }?.run { karma.useDebuggableChrome() }
        usePhantomJS?.takeIf { it }?.run { karma.usePhantomJS() }
        useFirefox?.takeIf { it }?.run { karma.useFirefox() }
        useFirefoxHeadless?.takeIf { it }?.run { karma.useFirefoxHeadless() }
        useFirefoxDeveloper?.takeIf { it }?.run { karma.useFirefoxDeveloper() }
        useFirefoxDeveloperHeadless?.takeIf { it }?.run { karma.useFirefoxDeveloperHeadless() }
        useFirefoxAurora?.takeIf { it }?.run { karma.useFirefoxAurora() }
        useFirefoxAuroraHeadless?.takeIf { it }?.run { karma.useFirefoxAuroraHeadless() }
        useFirefoxNightly?.takeIf { it }?.run { karma.useFirefoxNightly() }
        useFirefoxNightlyHeadless?.takeIf { it }?.run { karma.useFirefoxNightlyHeadless() }
        useOpera?.takeIf { it }?.run { karma.useOpera() }
        useSafari?.takeIf { it }?.run { karma.useSafari() }
        useIe?.takeIf { it }?.run { karma.useIe() }
        useSourceMapSupport?.takeIf { it }?.run { karma.useSourceMapSupport() }
    }
}
