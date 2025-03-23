@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import com.diffplug.spotless.cpp.ClangFormatStep
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.ProjectNamed
import gradle.collection.SerializableAnyMap
import gradle.collection.act
import gradle.serialization.serializer.JsonPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project

@Serializable(with = FormatExtensionSerializer::class)
internal abstract class FormatExtension<T : com.diffplug.gradle.spotless.FormatExtension> {

    abstract val lineEnding: LineEnding?
    abstract val ratchetFrom: String?
    abstract val excludeSteps: Set<String>?
    abstract val excludePaths: Set<String>?
    abstract val encoding: String?
    abstract val target: Set<String>?
    abstract val targetExclude: Set<String>?
    abstract val targetExcludeIfContentContains: String?
    abstract val targetExcludeIfContentContainsRegex: String?
    abstract val replace: List<Replace>?
    abstract val replaceRegex: List<ReplaceRegex>?

    /** Removes trailing whitespace.  */
    abstract val trimTrailingWhitespace: Boolean?

    /** Ensures that files end with a single newline.  */
    abstract val endWithNewline: Boolean?

    /** Ensures that the files are indented using spaces.  */
    abstract val indentWithSpaces: @Serializable(with = IntentSerializer::class) Any?

    /** Ensures that the files are indented using tabs.  */
    abstract val indentWithTabs: @Serializable(with = IntentSerializer::class) Any?
    abstract val nativeCmd: List<NativeCmd>?
    abstract val licenseHeader: LicenseHeaderConfig?
    abstract val prettier: PrettierConfig?
    abstract val biome: BiomeGeneric?
    abstract val clangFormat: ClangFormatConfig?
    abstract val eclipseWtp: EclipseWtpConfig?
    abstract val toggleOffOnRegex: String?

    /** Disables formatting between the given tags.  */
    abstract val toggleOffOn: @Serializable(with = ToggleOffOnSerializer::class) Any?

    /**
     * Undoes all previous calls to [.toggleOffOn] and
     * [.toggleOffOn].
     */
    abstract val toggleOffOnDisable: Boolean?

    context(Project)
    open fun applyTo(receiver: T) {
        lineEnding?.let(receiver::setLineEndings)
        ratchetFrom?.let(receiver::setRatchetFrom)
        excludeSteps?.forEach(receiver::ignoreErrorForStep)
        excludePaths?.forEach(receiver::ignoreErrorForPath)
        encoding?.let(receiver::setEncoding)
        target?.toTypedArray().let(receiver::target)
        targetExclude?.toTypedArray()?.let(receiver::targetExclude)
        targetExcludeIfContentContains?.let(receiver::targetExcludeIfContentContains)
        targetExcludeIfContentContainsRegex?.let(receiver::targetExcludeIfContentContainsRegex)
        replace?.forEach { (name, original, after) -> receiver.replace(name, original, after) }
        replaceRegex?.forEach { (name, regex, replacement) -> receiver.replaceRegex(name, regex, replacement) }
        trimTrailingWhitespace?.takeIf { it }?.run { receiver.trimTrailingWhitespace() }
        endWithNewline?.takeIf { it }?.run { receiver.endWithNewline() }

        when (val indentWithSpaces = indentWithSpaces) {
            is Boolean -> indentWithSpaces.takeIf { it }?.let { receiver.indentWithSpaces() }
            is Int -> receiver.indentWithSpaces(indentWithSpaces)

            else -> Unit
        }

        when (val indentWithTabs = indentWithTabs) {
            is Boolean -> indentWithTabs.takeIf { it }?.let { receiver.indentWithTabs() }
            is Int -> receiver.indentWithTabs(indentWithTabs)

            else -> Unit
        }

        nativeCmd?.forEach { (name, pathToExe, arguments) -> receiver.nativeCmd(name, pathToExe, arguments) }

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
                        ?: settings.libs.versions.version("biome"),
                ) as com.diffplug.gradle.spotless.FormatExtension.BiomeGeneric,
            )
        }

        clangFormat?.let { clangFormat ->
            clangFormat.applyTo(
                receiver.clangFormat(
                    clangFormat.version ?: settings.libs.versions.version("clang")
                    ?: ClangFormatStep.defaultVersion(),
                ),
            )
        }

        eclipseWtp?.let { eclipseWtp ->
            eclipseWtp.applyTo(
                receiver.eclipseWtp(
                    eclipseWtp.type,
                    eclipseWtp.version ?: settings.libs.versions.version("eclipseWtp")
                    ?: EclipseWtpFormatterStep.defaultVersion(),
                ),
            )
        }
        toggleOffOnRegex?.let(receiver::toggleOffOnRegex)
        when (val toggleOffOn = toggleOffOn) {
            is Boolean -> toggleOffOn.takeIf { it }?.run { receiver.toggleOffOn() }
            is ToggleOffOn -> receiver.toggleOffOn(toggleOffOn.off, toggleOffOn.on)

            else -> Unit
        }

        toggleOffOnDisable?.takeIf { it }?.run { receiver.toggleOffOnDisable() }
    }

    context(Project)
    abstract fun applyTo()

    private object IntentSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
            if (element.jsonPrimitive.content.matches("\\d+".toRegex()))
                Int.serializer()
            else Boolean.serializer()
    }

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
            pathToExe?.let(receiver::pathToExe)
            style?.let(receiver::style)
        }
    }

    @Serializable
    internal data class EclipseWtpConfig(
        val version: String? = null,
        val type: EclipseWtpFormatterStep,
        val configFiles: Set<String>? = null,
    ) {

        fun applyTo(receiver: com.diffplug.gradle.spotless.FormatExtension.EclipseWtpConfig) {
            configFiles?.toTypedArray()?.let(receiver::configFile)
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
            name?.let(receiver::named)
            contentPattern?.let(receiver::onlyIfContentMatches)
            yearSeparator?.let(receiver::yearSeparator)
            skipLinesMatching?.let(receiver::skipLinesMatching)
            updateYearWithLatest?.let(receiver::updateYearWithLatest)
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
            configFile?.let(receiver::configFile)
            config?.let(receiver::config)
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
        val off: String,
        val on: String
    )

    internal object ToggleOffOnSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
            if (element is JsonPrimitive) Boolean.serializer() else ToggleOffOn.serializer()
    }
}

private object FormatExtensionSerializer : JsonPolymorphicSerializer<FormatExtension<*>>(
    FormatExtension::class,
)

internal object FormatExtensionTransformingSerializer : KeyTransformingSerializer<FormatExtension<*>>(
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
    override val target: Set<String>? = null,
    override val targetExclude: Set<String>? = null,
    override val targetExcludeIfContentContains: String? = null,
    override val targetExcludeIfContentContainsRegex: String? = null,
    override val replace: List<Replace>? = null,
    override val replaceRegex: List<Replace>? = null,
    override val trimTrailingWhitespace: Boolean? = null,
    override val endWithNewline: Boolean? = null,
    override val indentWithSpaces: Int? = null,
    override val indentWithTabs: Int? = null,
    override val nativeCmd: List<NativeCmd>? = null,
    override val licenseHeader: LicenseHeaderConfig? = null,
    override val prettier: PrettierConfig? = null,
    override val biome: BiomeGeneric? = null,
    override val clangFormat: ClangFormatConfig? = null,
    override val eclipseWtp: EclipseWtpConfig? = null,
    override val toggleOffOnRegex: String? = null,
    override val toggleOffOn: ToggleOffOn? = null,
    override val toggleOffOnDisable: Boolean? = null,
) : FormatExtension<com.diffplug.gradle.spotless.FormatExtension>() {

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun applyTo() =
        if (name.isEmpty()) {
            spotless::class.memberProperties
                .single { property -> property.name == "formats" }
                .let { property ->
                    property.isAccessible = true
                    property.call(spotless) as Map<String, com.diffplug.gradle.spotless.FormatExtension>
                }.values.forEach { format ->
                    applyTo(format)
                }
        }
        else spotless.format(name) {
            applyTo(this)
        }
}
