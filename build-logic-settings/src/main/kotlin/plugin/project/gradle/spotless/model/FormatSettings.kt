package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.spotless.LineEnding
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class FormatSettings(
    override val lineEnding: LineEnding? = null,
    override val ratchetFrom: String? = null,
    override val excludeSteps: MutableSet<String>? = null,
    override val excludePaths: MutableSet<String>? = null,
    override val encoding: String? = null,
    override val target: List<String>? = null,
    override val targetExclude: List<String>? = listOf(
        "**/generated-src/**",
        "**/build-*/**",
        "**/.idea/**",
        "**/.fleet/**",
        "**/.idea/**",
        "**/.gradle/**",
        "/spotless/**",
        "**/resources/**",
        "**/buildSrc/**",
    ),
    override val targetExcludeIfContentContains: String? = null,
    override val targetExcludeIfContentContainsRegex: String? = null,
    override val replace: List<Replace>? = null,
    override val replaceRegex: List<Replace>? = null,
    /** Removes trailing whitespace.  */
    override val trimTrailingWhitespace: Boolean? = null,
    /** Ensures that files end with a single newline.  */
    override val endWithNewline: Boolean? = null,
    /** Ensures that the files are indented using spaces.  */
    override val indentWithSpaces: Int? = null,
    /** Ensures that the files are indented using spaces.  */
    override val indentIfWithSpaces: Boolean? = null,
    /** Ensures that the files are indented using tabs.  */
    override val indentWithTabs: Int? = null,
    /** Ensures that the files are indented using tabs.  */
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
    override val importOrder: ImportOrderConfig? = null,
    override val removeIfUnusedImports: Boolean? = null,
    override val removeUnusedImports: String? = null,
    override val googleJavaFormat: GoogleJavaFormatConfig? = null,
    override val palantirJavaFormat: PalantirJavaFormatConfig? = null,
    override val eclipse: EclipseConfig? = null,
    override val formatAnnotations: FormatAnnotationsConfig? = null,
    override val cleanthat: CleanthatJavaConfig? = null,
    override val diktat: DiktatConfig? = null,
    override val ktfmt: List<KtfmtConfig>? = null,
    override val ktlint: KtlintConfig? = null,
) : FormatExtension, BaseKotlinExtension, JavaExtension {

    context(Project)
    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super<FormatExtension>.applyTo(extension)

        when (extension) {
            is com.diffplug.gradle.spotless.JavaExtension -> super<JavaExtension>.applyTo(extension)

            is KotlinExtension -> super<BaseKotlinExtension>.applyTo(extension)
        }
    }
}
