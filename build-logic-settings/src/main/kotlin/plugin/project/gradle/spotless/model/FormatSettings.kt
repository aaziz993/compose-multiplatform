package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.spotless.LineEnding
import kotlinx.serialization.Serializable

@Serializable
internal data class FormatSettings(
    val enabled: Boolean = true,
    override val diktat: DiktatConfig? = null,
    override val ktfmt: List<KtfmtConfig>? = null,
    override val ktlint: KtlintConfig? = null,
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
    override val clangFormat: ClangFormatConfig? = null,
    val importOrder: ImportOrderConfig? = null,
    val removeIfUnusedImports: Boolean? = null,
    val removeUnusedImports: String? = null,
    /** Uses the [google-java-format](https://github.com/google/google-java-format) jar to format source code.  */
    val googleJavaFormat: GoogleJavaFormatConfig? = null,
    /** Uses the [palantir-java-format](https://github.com/palantir/palantir-java-format) jar to format source code.  */
    val palantirJavaFormat: PalantirJavaFormatConfig? = null,
    val eclipse: EclipseConfig? = null,
    val eclipseWtp: EclipseWtpConfig? = null,
    /** Removes newlines between type annotations and types.  */
    val formatAnnotations: FormatAnnotationsConfig? = null,
    /** Apply CleanThat refactoring rules.  */
    val cleanthat: CleanthatJavaConfig? = null,
    val toggleOffOnRegex: String? = null,
    /** Disables formatting between the given tags.  */
    val toggleOffOn: ToggleOffOn? = null,
    /** Disables formatting between `spotless:off` and `spotless:on`.  */
    val toggleIfOffOn: Boolean? = null,
    /**
     * Undoes all previous calls to [.toggleOffOn] and
     * [.toggleOffOn].
     */
    val toggleOffOnDisable: Boolean? = null
) : FormatExtension {

    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)
        if (extension is JavaExtension) {
            importOrder?.let { importOrder ->
                importOrder.applyTo(
                    importOrder.importOrder?.let { extension.importOrder(*it.toTypedArray()) }
                        ?: importOrder.importOrderFile?.let(extension::importOrderFile)!!,
                )
            }

            removeIfUnusedImports?.let { extension.removeUnusedImports() }
            removeUnusedImports?.let(extension::removeUnusedImports)

            googleJavaFormat?.let { googleJavaFormat ->
                googleJavaFormat.applyTo(
                    googleJavaFormat.version?.let(extension::googleJavaFormat)
                        ?: extension.googleJavaFormat(),
                )
            }

            palantirJavaFormat?.let { palantirJavaFormat ->
                palantirJavaFormat.applyTo(
                    palantirJavaFormat.version?.let(extension::palantirJavaFormat)
                        ?: extension.palantirJavaFormat(),
                )
            }

            eclipse?.let { eclipse ->
                eclipse.applyTo(eclipse.formatterVersion?.let(extension::eclipse) ?: extension.eclipse())
            }

            formatAnnotations?.let { formatAnnotations ->
                formatAnnotations.applyTo(extension.formatAnnotations())
            }

            cleanthat?.let { cleanthat ->
                cleanthat.applyTo(extension.cleanthat())
            }
        }

        (extension as? KotlinExtension)?.let(::applyTo)

        eclipseWtp?.let { eclipseWtp ->
            eclipseWtp.applyTo(
                eclipseWtp.version?.let { extension.eclipseWtp(eclipseWtp.type, it) }
                    ?: extension.eclipseWtp(eclipseWtp.type),
            )
        }
        toggleOffOnRegex?.let(extension::toggleOffOnRegex)
        toggleOffOn?.let { (off, on) -> extension.toggleOffOn(off, on) }
        toggleIfOffOn?.takeIf { it }.run { extension.toggleOffOn() }
        toggleOffOnDisable?.takeIf { it }.run { extension.toggleOffOnDisable() }
    }
}
