@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import gradle.accessors.allLibs
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = FormatExtensionSerializer::class)
internal abstract class FormatExtension {

    abstract val lineEnding: LineEnding?
    abstract val ratchetFrom: String?
    abstract val excludeSteps: Set<String>?
    abstract val excludePaths: Set<String>?
    abstract val encoding: String?
    abstract val target: List<String>?
    abstract val targetExclude: List<String>?
    abstract val targetExcludeIfContentContains: String?
    abstract val targetExcludeIfContentContainsRegex: String?
    abstract val replace: List<Replace>?
    abstract val replaceRegex: List<Replace>?

    /** Removes trailing whitespace.  */
    abstract val trimTrailingWhitespace: Boolean?

    /** Ensures that files end with a single newline.  */
    abstract val endWithNewline: Boolean?

    /** Ensures that the files are indented using spaces.  */
    abstract val indentWithSpaces: Int?

    /** Ensures that the files are indented using spaces.  */
    abstract val indentIfWithSpaces: Boolean?

    /** Ensures that the files are indented using tabs.  */
    abstract val indentWithTabs: Int?

    /** Ensures that the files are indented using tabs.  */
    abstract val indentIfWithTabs: Boolean?
    abstract val nativeCmd: List<NativeCmd>?
    abstract val licenseHeader: LicenseHeaderConfig?
    abstract val prettier: PrettierConfig?
    abstract val biome: BiomeGeneric?
    abstract val clangFormat: ClangFormatConfig?
    abstract val eclipseWtp: EclipseWtpConfig?
    abstract val toggleOffOnRegex: String?

    /** Disables formatting between the given tags.  */
    abstract val toggleOffOn: ToggleOffOn?

    /** Disables formatting between `spotless:off` and `spotless:on`.  */
    abstract val toggleIfOffOn: Boolean?

    /**
     * Undoes all previous calls to [.toggleOffOn] and
     * [.toggleOffOn].
     */
    abstract val toggleOffOnDisable: Boolean?

    context(Project)
    open fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        lineEnding?.let(extension::setLineEndings)
        ratchetFrom?.let(extension::setRatchetFrom)
        excludeSteps?.forEach(extension::ignoreErrorForStep)
        excludePaths?.forEach(extension::ignoreErrorForPath)
        encoding?.let(extension::setEncoding)
        target?.let { extension.target(*it.toTypedArray()) }
        targetExclude?.let { targetExclude ->
            extension.targetExclude(*targetExclude.toTypedArray())
        }
        targetExcludeIfContentContains?.let(extension::targetExcludeIfContentContains)
        targetExcludeIfContentContainsRegex?.let(extension::targetExcludeIfContentContainsRegex)
        replace?.forEach { (name, original, replacement) -> extension.replace(name, original, replacement) }
        replaceRegex?.forEach { (name, regex, replacement) -> extension.replaceRegex(name, regex, replacement) }
        trimTrailingWhitespace?.takeIf { it }?.run { extension.trimTrailingWhitespace() }
        endWithNewline?.takeIf { it }?.run { extension.endWithNewline() }
        indentWithSpaces?.let(extension::indentWithSpaces)
        indentIfWithSpaces?.takeIf { it }?.let { extension.indentWithSpaces() }
        indentWithTabs?.let(extension::indentWithTabs)
        indentIfWithTabs?.takeIf { it }?.run { extension.indentWithTabs() }
        nativeCmd?.forEach { (name, pathToExe, arguments) -> extension.nativeCmd(name, pathToExe, arguments) }
        licenseHeader?.let { license ->
            license.applyTo(
                license.header?.let { extension.licenseHeader(it, license.delimiter) }
                    ?: extension.licenseHeader(license.headerFile!!, license.delimiter),
            )
        }
        prettier?.let { prettier ->
            extension.prettier(prettier.devDependencies)
        }

        biome?.let { biome ->
            biome.applyTo(
                biome.version?.resolveVersion()?.let(extension::biome) ?: extension.biome(),
            )
        }

        clangFormat?.let { clangFormat ->
            clangFormat.applyTo(
                clangFormat.version?.resolveVersion()?.let(extension::clangFormat)
                    ?: extension.clangFormat(),
            )
        }

        eclipseWtp?.let { eclipseWtp ->
            eclipseWtp.applyTo(
                (eclipseWtp.version?.resolveVersion() ?: settings.libs.versions.version("eclipseWtp"))
                    ?.let { extension.eclipseWtp(eclipseWtp.type, it) }
                    ?: extension.eclipseWtp(eclipseWtp.type),
            )
        }
        toggleOffOnRegex?.let(extension::toggleOffOnRegex)
        toggleOffOn?.let { (off, on) -> extension.toggleOffOn(off, on) }
        toggleIfOffOn?.takeIf { it }?.run { extension.toggleOffOn() }
        toggleOffOnDisable?.takeIf { it }?.run { extension.toggleOffOnDisable() }
    }

    context(Project)
    abstract fun applyTo()

    @Serializable
    internal data class ClangFormatConfig(
        val version: String? = null,
        val pathToExe: String? = null,
        val style: String? = null,
    ) {

        fun applyTo(format: com.diffplug.gradle.spotless.FormatExtension.ClangFormatConfig) {
            pathToExe?.let(format::pathToExe)
            style?.let(format::style)
        }
    }

    @Serializable
    internal data class EclipseWtpConfig(
        val version: String? = null,
        val type: EclipseWtpFormatterStep,
        val settingsFiles: List<String>? = null,
    ) {

        fun applyTo(eclipse: com.diffplug.gradle.spotless.FormatExtension.EclipseWtpConfig) {
            settingsFiles?.let { eclipse.configFile(*it.toTypedArray()) }
        }
    }

    @Serializable
    internal data class LicenseHeaderConfig(
        val name: String? = null,
        val contentPattern: String? = null,
        val header: String? = null,
        val headerFile: String? = null,
        val delimiter: String? = null,
        val yearSeparator: String? = null,
        val skipLinesMatching: String? = null,
        val updateYearWithLatest: Boolean? = null,
    ) {

        fun applyTo(license: com.diffplug.gradle.spotless.FormatExtension.LicenseHeaderConfig) {
            name?.let(license::named)
            contentPattern?.let(license::onlyIfContentMatches)
            yearSeparator?.let(license::yearSeparator)
            skipLinesMatching?.let(license::skipLinesMatching)
            updateYearWithLatest?.let(license::updateYearWithLatest)
        }
    }

    @Serializable
    internal data class NativeCmd(
        val name: String,
        val pathToExe: String,
        val arguments: List<String> = emptyList()
    )

    @Serializable
    internal data class PrettierConfig(
        val devDependencies: MutableMap<String, String>? = null
    )

    @Serializable
    internal data class Replace(
        val name: String,
        val original: String,
        val replacement: String
    )

    @Serializable
    internal data class ToggleOffOn(
        val off: String,
        val on: String
    )
}

private object FormatExtensionSerializer : JsonContentPolymorphicSerializer<FormatExtension>(
    FormatExtension::class,
)

internal object FormatExtensionTransformingSerializer : KeyTransformingSerializer<FormatExtension>(
    FormatExtension.serializer(),
    "type",
)
