package gradle.plugins.spotless.groovy

import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
import gradle.plugins.spotless.BiomeGeneric
import gradle.plugins.spotless.HasBuiltinDelimiterForLicense
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** Configures the special groovy-specific extension.  */
@Serializable
@SerialName("groovy")
internal data class GroovyExtension(
    override val lineEnding: LineEnding? = null,
    override val ratchetFrom: String? = null,
    override val excludeSteps: MutableSet<String>? = null,
    override val excludePaths: MutableSet<String>? = null,
    override val encoding: String? = null,
    override val target: List<String>? = null,
    override val targetExclude: List<String>? = null,
    override val targetExcludeIfContentContains: String? = null,
    override val targetExcludeIfContentContainsRegex: String? = null,
    override val replace: List<Replace>? = null,
    override val replaceRegex: List<Replace>? = null,
    override val trimTrailingWhitespace: Boolean? = null,
    override val endWithNewline: Boolean? = null,
    override val indentWithSpaces: Int? = null,
    override val indentIfWithSpaces: Boolean? = null,
    override val indentWithTabs: Int? = null,
    override val indentIfWithTabs: Boolean? = null,
    override val nativeCmd: List<NativeCmd>? = null,
    override val licenseHeader: LicenseHeaderConfig? = null,
    override val prettier: PrettierConfig? = null,
    override val biome: BiomeGeneric? = null,
    override val clangFormat: ClangFormatConfig? = null,
    override val eclipseWtp: EclipseWtpConfig? = null,
    override val toggleOffOnRegex: String? = null,
    override val toggleOffOn: ToggleOffOn? = null,
    override val toggleIfOffOn: Boolean? = null,
    override val toggleOffOnDisable: Boolean? = null,
) : BaseGroovyExtension(), HasBuiltinDelimiterForLicense {

    context(Project)
    override fun applyTo() = spotless.groovy {
        super<BaseGroovyExtension>.applyTo(this)
        super<HasBuiltinDelimiterForLicense>.applyTo(this)
    }
}
