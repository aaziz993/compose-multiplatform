package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.spotless.LineEnding
import gradle.spotless
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
@SerialName("java")
internal data class JavaExtension(
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
    val importOrder: ImportOrderConfig? = null,
    val removeIfUnusedImports: Boolean? = null,
    val removeUnusedImports: String? = null,
    /** Uses the [google-java-format](https://github.com/google/google-java-format) jar to format source code.  */
    val googleJavaFormat: GoogleJavaFormatConfig? = null,
    /** Uses the [palantir-java-format](https://github.com/palantir/palantir-java-format) jar to format source code.  */
    val palantirJavaFormat: PalantirJavaFormatConfig? = null,
    val eclipse: EclipseConfig? = null,
    /** Removes newlines between type annotations and types.  */
    val formatAnnotations: FormatAnnotationsConfig? = null,
    /** Apply CleanThat refactoring rules.  */
    val cleanthat: CleanthatJavaConfig? = null,
) : FormatExtension {

    context(Project)
    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)

        extension as JavaExtension

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
                googleJavaFormat.version?.resolveVersion()?.let(extension::googleJavaFormat)
                    ?: extension.googleJavaFormat(),
            )
        }

        palantirJavaFormat?.let { palantirJavaFormat ->
            palantirJavaFormat.applyTo(
                palantirJavaFormat.version?.resolveVersion()?.let(extension::palantirJavaFormat)
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

    context(Project)
    override fun applyTo() = spotless.java {
        applyTo(this)
    }
}
