@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.serialization.serializer.JsonPolymorphicSerializer
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
    open fun applyTo(recipient: com.diffplug.gradle.spotless.FormatExtension) {
        lineEnding?.let(recipient::setLineEndings)
        ratchetFrom?.let(recipient::setRatchetFrom)
        excludeSteps?.forEach(recipient::ignoreErrorForStep)
        excludePaths?.forEach(recipient::ignoreErrorForPath)
        encoding?.let(recipient::setEncoding)
        target?.toTypedArray().let(recipient::target)
        targetExclude?.toTypedArray()?.let(recipient::targetExclude)
        targetExcludeIfContentContains?.let(recipient::targetExcludeIfContentContains)
        targetExcludeIfContentContainsRegex?.let(recipient::targetExcludeIfContentContainsRegex)
        replace?.forEach { (name, original, replacement) -> recipient.replace(name, original, replacement) }
        replaceRegex?.forEach { (name, regex, replacement) -> recipient.replaceRegex(name, regex, replacement) }
        trimTrailingWhitespace?.takeIf { it }?.run { recipient.trimTrailingWhitespace() }
        endWithNewline?.takeIf { it }?.run { recipient.endWithNewline() }
        indentWithSpaces?.let(recipient::indentWithSpaces)
        indentIfWithSpaces?.takeIf { it }?.let { recipient.indentWithSpaces() }
        indentWithTabs?.let(recipient::indentWithTabs)
        indentIfWithTabs?.takeIf { it }?.run { recipient.indentWithTabs() }
        nativeCmd?.forEach { (name, pathToExe, arguments) -> recipient.nativeCmd(name, pathToExe, arguments) }
        licenseHeader?.let { license ->
            license.applyTo(
                license.header?.let { recipient.licenseHeader(it, license.delimiter) }
                    ?: recipient.licenseHeader(license.headerFile!!, license.delimiter),
            )
        }
        prettier?.let { prettier ->
            recipient.prettier(prettier.devDependencies)
        }

        biome?.let { biome ->
            biome.applyTo(
                biome.version?.resolveVersion()?.let(recipient::biome) ?: recipient.biome(),
            )
        }

        clangFormat?.let { clangFormat ->
            clangFormat.applyTo(
                clangFormat.version?.resolveVersion()?.let(recipient::clangFormat)
                    ?: recipient.clangFormat(),
            )
        }

        eclipseWtp?.let { eclipseWtp ->
            eclipseWtp.applyTo(
                (eclipseWtp.version?.resolveVersion() ?: settings.libs.versions.version("eclipseWtp"))
                    ?.let { recipient.eclipseWtp(eclipseWtp.type, it) }
                    ?: recipient.eclipseWtp(eclipseWtp.type),
            )
        }
        toggleOffOnRegex?.let(recipient::toggleOffOnRegex)
        toggleOffOn?.let { (off, on) -> recipient.toggleOffOn(off, on) }
        toggleIfOffOn?.takeIf { it }?.run { recipient.toggleOffOn() }
        toggleOffOnDisable?.takeIf { it }?.run { recipient.toggleOffOnDisable() }
    }

    context(Project)
    abstract fun applyTo()

    @Serializable
    internal data class ClangFormatConfig(
        val version: String? = null,
        val pathToExe: String? = null,
        val style: String? = null,
    ) {

        fun applyTo(recipient: com.diffplug.gradle.spotless.FormatExtension.ClangFormatConfig) {
            pathToExe?.let(recipient::pathToExe)
            style?.let(recipient::style)
        }
    }

    @Serializable
    internal data class EclipseWtpConfig(
        val version: String? = null,
        val type: EclipseWtpFormatterStep,
        val settingsFiles: List<String>? = null,
    ) {

        fun applyTo(recipient: com.diffplug.gradle.spotless.FormatExtension.EclipseWtpConfig) {
            settingsFiles?.toTypedArray()?.let(recipient::configFile)
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

        fun applyTo(recipient: com.diffplug.gradle.spotless.FormatExtension.LicenseHeaderConfig) {
            name?.let(recipient::named)
            contentPattern?.let(recipient::onlyIfContentMatches)
            yearSeparator?.let(recipient::yearSeparator)
            skipLinesMatching?.let(recipient::skipLinesMatching)
            updateYearWithLatest?.let(recipient::updateYearWithLatest)
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

private object FormatExtensionSerializer : JsonPolymorphicSerializer<FormatExtension>(
    FormatExtension::class,
)

internal object FormatExtensionTransformingSerializer : KeyTransformingSerializer<FormatExtension>(
    FormatExtension.serializer(),
    "type",
)
