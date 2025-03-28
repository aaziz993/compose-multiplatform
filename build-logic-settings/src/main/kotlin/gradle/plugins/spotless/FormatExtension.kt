@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import com.diffplug.spotless.cpp.ClangFormatStep
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import com.diffplug.spotless.generic.PipeStepPair
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.ifTrue
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = FormatExtensionSerializer::class)
internal abstract class FormatExtension<T : com.diffplug.gradle.spotless.FormatExtension> {

    abstract val lineEnding: LineEnding?
    abstract val ratchetFrom: String?
    abstract val excludeSteps: Set<String>?
    abstract val excludePaths: Set<String>?
    abstract val encoding: String?
    abstract val targets: Set<String>?
    abstract val targetExcludes: Set<String>?
    abstract val targetExcludeIfContentContains: String?
    abstract val targetExcludeIfContentContainsRegex: String?
    abstract val replaces: List<Replace>?
    abstract val replaceRegexes: List<ReplaceRegex>?

    /** Removes trailing whitespace.  */
    abstract val trimTrailingWhitespace: Boolean?

    /** Ensures that files end with a single newline.  */
    abstract val endWithNewline: Boolean?

    /** Ensures that the files are indented using spaces.  */
    abstract val indentWithSpaces: Int?

    /** Ensures that the files are indented using tabs.  */
    abstract val indentWithTabs: Int?
    abstract val nativeCmds: List<NativeCmd>?
    abstract val licenseHeader: LicenseHeaderConfig?
    abstract val prettier: PrettierConfig?
    abstract val biome: BiomeGeneric?
    abstract val clangFormat: ClangFormatConfig?
    abstract val eclipseWtp: EclipseWtpConfig?
    abstract val toggleOffOnRegex: String?

    /** Disables formatting between the given tags.  */
    abstract val toggleOffOn: ToggleOffOn?

    /**
     * Undoes all previous calls to [.toggleOffOn] and
     * [.toggleOffOn].
     */
    abstract val toggleOffOnDisable: Boolean?

    context(Project)
    open fun applyTo(receiver: T) {
        receiver::setLineEndings trySet lineEnding
        receiver::setRatchetFrom trySet ratchetFrom
        excludeSteps?.forEach(receiver::ignoreErrorForStep)
        excludePaths?.forEach(receiver::ignoreErrorForPath)
        encoding?.let(receiver::setEncoding)
        receiver::target trySet targets
        receiver::targetExclude trySet targetExcludes
        receiver::targetExcludeIfContentContains trySet targetExcludeIfContentContains
        receiver::targetExcludeIfContentContainsRegex trySet targetExcludeIfContentContainsRegex
        replaces?.forEach { (name, original, after) -> receiver.replace(name, original, after) }
        replaceRegexes?.forEach { (name, regex, replacement) -> receiver.replaceRegex(name, regex, replacement) }
        trimTrailingWhitespace?.ifTrue(receiver::trimTrailingWhitespace)
        endWithNewline?.ifTrue(receiver::endWithNewline)
        indentWithSpaces?.let(receiver::indentWithSpaces)
        indentWithTabs?.let(receiver::indentWithTabs)

        nativeCmds?.forEach { (name, pathToExe, arguments) -> receiver.nativeCmd(name, pathToExe, arguments) }

        licenseHeader?.let { license ->
            license.applyTo(
                license.header?.let { receiver.licenseHeader(it, license.delimiter) }
                    ?: receiver.licenseHeaderFile(license.headerFile!!, license.delimiter),
            )
        }

        prettier?.let { prettier ->
            prettier.applyTo(receiver.prettier(prettier.devDependencies))
        }

        biome?.let { biome ->
            biome.applyTo(
                receiver.biome(
                    biome.version
                        ?: project.settings.libs.versions.version("biome"),
                ) as com.diffplug.gradle.spotless.FormatExtension.BiomeGeneric,
            )
        }

        clangFormat?.let { clangFormat ->
            clangFormat.applyTo(
                receiver.clangFormat(
                    clangFormat.version ?: project.settings.libs.versions.version("clang")
                    ?: ClangFormatStep.defaultVersion(),
                ),
            )
        }

        eclipseWtp?.let { eclipseWtp ->
            eclipseWtp.applyTo(
                receiver.eclipseWtp(
                    eclipseWtp.type,
                    eclipseWtp.version ?: project.settings.libs.versions.version("eclipseWtp")
                    ?: EclipseWtpFormatterStep.defaultVersion(),
                ),
            )
        }

        receiver::toggleOffOnRegex trySet toggleOffOnRegex

        toggleOffOn?.let { (off, on) ->
            receiver.toggleOffOn(off, on)
        }

        toggleOffOnDisable?.ifTrue(receiver::toggleOffOnDisable)
    }

    context(Project)
    abstract fun applyTo()

    @Serializable
    internal data class BiomeGeneric(
        override val configPath: String? = null,
        override val downloadDir: String? = null,
        override val pathToExe: String? = null,
        override val version: String? = null,
    ) : RomeStepConfig<com.diffplug.gradle.spotless.FormatExtension.BiomeGeneric>

    @Serializable
    internal data class ClangFormatConfig(
        val version: String? = null,
        val pathToExe: String? = null,
        val style: String? = null,
    ) {

        fun applyTo(receiver: com.diffplug.gradle.spotless.FormatExtension.ClangFormatConfig) {
            receiver::pathToExe trySet pathToExe
            receiver::style trySet style
        }
    }

    @Serializable
    internal data class EclipseWtpConfig(
        val version: String? = null,
        val type: EclipseWtpFormatterStep,
        val configFiles: Set<String>? = null,
    ) {

        fun applyTo(receiver: com.diffplug.gradle.spotless.FormatExtension.EclipseWtpConfig) {
            receiver::configFile trySet configFiles
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

        fun applyTo(receiver: com.diffplug.gradle.spotless.FormatExtension.LicenseHeaderConfig) {
            receiver::named trySet name
            receiver::onlyIfContentMatches trySet contentPattern
            receiver::yearSeparator trySet yearSeparator
            receiver::skipLinesMatching trySet skipLinesMatching
            receiver::updateYearWithLatest trySet updateYearWithLatest
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
        val devDependencies: Map<String, String>? = null,
        val configFile: String? = null,
        val config: SerializableAnyMap? = null
    ) {

        fun applyTo(receiver: com.diffplug.gradle.spotless.FormatExtension.PrettierConfig) {
            receiver::configFile trySet configFile
            receiver::config trySet config
        }
    }

    @Serializable
    internal data class Replace(
        val name: String,
        val original: String,
        val after: String
    )

    @Serializable
    internal data class ReplaceRegex(
        val name: String,
        val regex: String,
        val replacement: String
    )

    @Serializable
    internal data class ToggleOffOn(
        val off: String = PipeStepPair.defaultToggleOff(),
        val on: String = PipeStepPair.defaultToggleOn()
    )
}

private object FormatExtensionSerializer : JsonPolymorphicSerializer<FormatExtension<*>>(
    FormatExtension::class,
)

internal object FormatExtensionKeyTransformingSerializer : KeyTransformingSerializer<FormatExtension<*>>(
    FormatExtensionSerializer,
    "type",
)

@Serializable
@SerialName("format")
internal data class FormatExtensionImpl(
    override val lineEnding: LineEnding? = null,
    override val ratchetFrom: String? = null,
    override val excludeSteps: MutableSet<String>? = null,
    override val excludePaths: MutableSet<String>? = null,
    override val encoding: String? = null,
    override val targets: Set<String>? = null,
    override val targetExcludes: Set<String>? = null,
    override val targetExcludeIfContentContains: String? = null,
    override val targetExcludeIfContentContainsRegex: String? = null,
    override val replaces: List<Replace>? = null,
    override val replaceRegexes: List<ReplaceRegex>? = null,
    override val trimTrailingWhitespace: Boolean? = null,
    override val endWithNewline: Boolean? = null,
    override val indentWithSpaces: Int? = null,
    override val indentWithTabs: Int? = null,
    override val nativeCmds: List<NativeCmd>? = null,
    override val licenseHeader: LicenseHeaderConfig? = null,
    override val prettier: PrettierConfig? = null,
    override val biome: BiomeGeneric? = null,
    override val clangFormat: ClangFormatConfig? = null,
    override val eclipseWtp: EclipseWtpConfig? = null,
    override val toggleOffOnRegex: String? = null,
    override val toggleOffOn: ToggleOffOn? = null,
    override val toggleOffOnDisable: Boolean? = null,
    val name: String? = null,
) : FormatExtension<com.diffplug.gradle.spotless.FormatExtension>() {

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun applyTo() =
        if (name.isNullOrBlank()) {
            project.spotless::class.memberProperties
                .single { property -> property.name == "formats" }
                .let { property ->
                    property.isAccessible = true
                    property.call(project.spotless) as Map<String, com.diffplug.gradle.spotless.FormatExtension>
                }.values.forEach { format ->
                    applyTo(format)
                }
        }
        else project.spotless.format(name) {
            applyTo(this)
        }
}
