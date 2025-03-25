package gradle.plugins.spotless.kotlin

import com.diffplug.gradle.spotless.KotlinGradleExtension
import com.diffplug.spotless.LineEnding
import gradle.accessors.spotless
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
@SerialName("kotlinGradle")
internal data class KotlinGradleExtension(
    override val diktat: DiktatConfig? = null,
    override val ktfmt: List<KtfmtConfig>? = null,
    override val ktlint: KtlintConfig? = null,
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
) : BaseKotlinExtension<KotlinGradleExtension>() {

    context(Project)
    override fun applyTo() = project.spotless.kotlinGradle {
        super.applyTo(this)
    }
}
