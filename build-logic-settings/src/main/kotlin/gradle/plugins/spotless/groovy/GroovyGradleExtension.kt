package gradle.plugins.spotless.groovy

import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
import gradle.plugins.spotless.BiomeGeneric
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** Configures the special groovy-specific extension for Gradle files.  */
@Serializable
@SerialName("groovyGradle")
internal data class GroovyGradleExtension(
    override val lineEnding: LineEnding? = null,
    override val ratchetFrom: String? = null,
    override val excludeSteps: Set<String>? = null,
    override val excludePaths: Set<String>? = null,
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
    override val importOrder: List<String>? = null,
    override val importOrderFile: String? = null,
    override val removeSemicolons: Boolean? = null,
    override val greclipse: GrEclipseConfig? = null,
) : BaseGroovyExtension() {

    context(Project)
    override fun applyTo() = spotless.groovyGradle {
        applyTo(this)
    }
}
