@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import gradle.accessors.allLibs
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.accessors.settings
import gradle.accessors.version
import gradle.api.version
import gradle.accessors.versions
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = FormatExtensionSerializer::class)
internal interface FormatExtension {

    val lineEnding: LineEnding?
    val ratchetFrom: String?
    val excludeSteps: Set<String>?
    val excludePaths: Set<String>?
    val encoding: String?
    val target: List<String>?
    val targetExclude: List<String>?
    val targetExcludeIfContentContains: String?
    val targetExcludeIfContentContainsRegex: String?
    val replace: List<Replace>?
    val replaceRegex: List<Replace>?

    /** Removes trailing whitespace.  */
    val trimTrailingWhitespace: Boolean?

    /** Ensures that files end with a single newline.  */
    val endWithNewline: Boolean?

    /** Ensures that the files are indented using spaces.  */
    val indentWithSpaces: Int?

    /** Ensures that the files are indented using spaces.  */
    val indentIfWithSpaces: Boolean?

    /** Ensures that the files are indented using tabs.  */
    val indentWithTabs: Int?

    /** Ensures that the files are indented using tabs.  */
    val indentIfWithTabs: Boolean?
    val nativeCmd: List<NativeCmd>?
    val licenseHeader: LicenseHeaderConfig?
    val prettier: PrettierConfig?
    val biome: BiomeGeneric?
    val clangFormat: ClangFormatConfig?
    val eclipseWtp: EclipseWtpConfig?
    val toggleOffOnRegex: String?

    /** Disables formatting between the given tags.  */
    val toggleOffOn: ToggleOffOn?

    /** Disables formatting between `spotless:off` and `spotless:on`.  */
    val toggleIfOffOn: Boolean?

    /**
     * Undoes all previous calls to [.toggleOffOn] and
     * [.toggleOffOn].
     */
    val toggleOffOnDisable: Boolean?

    context(Project)
    fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
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
    fun applyTo()

    context(Project)
    fun String.resolveVersion() =
        if (startsWith("$")) settings.allLibs.resolveVersion(this)
        else this
}

private object FormatExtensionSerializer : JsonContentPolymorphicSerializer<FormatExtension>(
    FormatExtension::class,
)

internal object FormatExtensionTransformingSerializer : KeyTransformingSerializer<FormatExtension>(
    FormatExtension.serializer(),
    "type",
)
