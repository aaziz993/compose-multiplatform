package plugin.project.web.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.testing.karma.KotlinKarma

@Serializable
internal data class KotlinKarma(
    val webpackConfig: KotlinWebpackConfig? = null,
    val configDirectory: String? = null,
    val chrome: Boolean? = null,
    val chromeHeadless: Boolean? = null,
    val chromeHeadlessNoSandbox: Boolean? = null,
    val chromium: Boolean? = null,
    val chromiumHeadless: Boolean? = null,
    val chromeCanary: Boolean? = null,
    val chromeCanaryHeadless: Boolean? = null,
    val debuggableChrome: Boolean? = null,
    val phantomJS: Boolean? = null,
    val firefox: Boolean? = null,
    val firefoxHeadless: Boolean? = null,
    val firefoxDeveloper: Boolean? = null,
    val firefoxDeveloperHeadless: Boolean? = null,
    val firefoxAurora: Boolean? = null,
    val firefoxAuroraHeadless: Boolean? = null,
    val firefoxNightly: Boolean? = null,
    val firefoxNightlyHeadless: Boolean? = null,
    val opera: Boolean? = null,
    val safari: Boolean? = null,
    val ie: Boolean? = null,
    val sourceMapSupport: Boolean? = null,
) {

    context(Project)
    fun applyTo(karma: KotlinKarma) {
        if (webpackConfig != null) {
            webpackConfig.applyTo(karma.webpackConfig)
        }

        configDirectory?.let(karma::useConfigDirectory)
        chrome?.takeIf { it }?.run { karma.useChrome() }
        chromeHeadless?.takeIf { it }?.run { karma.useChromeHeadless() }
        chromeHeadlessNoSandbox?.takeIf { it }?.run { karma.useChromeHeadlessNoSandbox() }
        chromium?.takeIf { it }?.run { karma.useChromium() }
        chromiumHeadless?.takeIf { it }?.run { karma.useChromiumHeadless() }
        chromeCanary?.takeIf { it }?.run { karma.useChromeCanary() }
        chromeCanaryHeadless?.takeIf { it }?.run { karma.useChromeCanaryHeadless() }
        debuggableChrome?.takeIf { it }?.run { karma.useDebuggableChrome() }
        phantomJS?.takeIf { it }?.run { karma.usePhantomJS() }
        firefox?.takeIf { it }?.run { karma.useFirefox() }
        firefoxHeadless?.takeIf { it }?.run { karma.useFirefoxHeadless() }
        firefoxDeveloper?.takeIf { it }?.run { karma.useFirefoxDeveloper() }
        firefoxDeveloperHeadless?.takeIf { it }?.run { karma.useFirefoxDeveloperHeadless() }
        firefoxAurora?.takeIf { it }?.run { karma.useFirefoxAurora() }
        firefoxAuroraHeadless?.takeIf { it }?.run { karma.useFirefoxAuroraHeadless() }
        firefoxNightly?.takeIf { it }?.run { karma.useFirefoxNightly() }
        firefoxNightlyHeadless?.takeIf { it }?.run { karma.useFirefoxNightlyHeadless() }
        opera?.takeIf { it }?.run { karma.useOpera() }
        safari?.takeIf { it }?.run { karma.useSafari() }
        ie?.takeIf { it }?.run { karma.useIe() }
        sourceMapSupport?.takeIf { it }?.run { karma.useSourceMapSupport() }
    }
}
