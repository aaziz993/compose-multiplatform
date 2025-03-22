package gradle.plugins.spotless

import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
@SerialName("format")
internal data class FormatExtensionImpl(
    val name: String = "",
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
    override fun applyTo() = if (name.isEmpty()) {
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
