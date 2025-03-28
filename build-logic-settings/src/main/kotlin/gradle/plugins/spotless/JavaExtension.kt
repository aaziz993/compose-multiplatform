package gradle.plugins.spotless

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.spotless.LineEnding
import gradle.accessors.catalog.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
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
    val importOrder: ImportOrderConfig? = null,
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
) : FormatExtension<JavaExtension>() {

    context(Project)
    override fun applyTo(receiver: JavaExtension) {
        super.applyTo(receiver)

        importOrder?.let { importOrder ->
            importOrder.applyTo(
                importOrder.importOrder?.let { receiver.importOrder(*it.toTypedArray()) }
                    ?: receiver.importOrderFile(importOrder.importOrderFile!!),
            )
        }

        removeUnusedImports?.let(receiver::removeUnusedImports)

        googleJavaFormat?.let { googleJavaFormat ->
            googleJavaFormat.applyTo(
                (googleJavaFormat.version?.resolveVersion()
                    ?: project.settings.libs.versions.version("googleJavaFormat"))
                    ?.let(receiver::googleJavaFormat) ?: receiver.googleJavaFormat(),
            )
        }

        palantirJavaFormat?.let { palantirJavaFormat ->
            palantirJavaFormat.applyTo(
                (palantirJavaFormat.version?.resolveVersion()
                    ?: project.settings.libs.versions.version("palantirJavaFormat"))
                    ?.let(receiver::palantirJavaFormat) ?: receiver.palantirJavaFormat(),
            )
        }

        eclipse?.let { eclipse ->
            eclipse.applyTo(
                (eclipse.formatterVersion?.resolveVersion()
                    ?: project.settings.libs.versions.version("eclipseFormatter"))
                    ?.let(receiver::eclipse) ?: receiver.eclipse(),
            )
        }

        formatAnnotations?.applyTo(receiver.formatAnnotations())
        cleanthat?.applyTo(receiver.cleanthat())
    }

    context(Project)
    override fun applyTo() = project.spotless.java {
        applyTo(this)
    }

    @Serializable
    internal class CleanthatJavaConfig(
        val groupArtifact: String? = null,
        val version: String? = null,
        val sourceJdk: String? = null,
        val mutators: Set<String?>? = null,
        val excludedMutators: Set<String?>? = null,
        val includeDraft: Boolean? = null
    ) {

        fun applyTo(format: JavaExtension.CleanthatJavaConfig) {
            groupArtifact?.let(format::groupArtifact)
            version?.let(format::version)
            sourceJdk?.let(format::sourceCompatibility)
            mutators?.let(format::addMutators)
            excludedMutators?.forEach(format::excludeMutator)
            includeDraft?.let(format::includeDraft)
        }
    }

    @Serializable
    internal data class EclipseConfig(
        val formatterVersion: String? = null,
        val configFiles: Set<String>? = null,
        val p2Mirrors: Map<String, String>? = null
    ) {

        fun applyTo(eclipse: JavaExtension.EclipseConfig) {
            eclipse::configFile trySet configFiles
            p2Mirrors?.let(eclipse::withP2Mirrors)
        }
    }

    @Serializable
    internal data class FormatAnnotationsConfig(
        val addedTypeAnnotations: Set<String>? = null,
        val removedTypeAnnotations: Set<String>? = null
    ) {

        fun applyTo(receiver: JavaExtension.FormatAnnotationsConfig) {
            addedTypeAnnotations?.forEach(receiver::addTypeAnnotation)
            removedTypeAnnotations?.forEach(receiver::removeTypeAnnotation)
        }
    }

    @Serializable
    internal data class GoogleJavaFormatConfig(
        val version: String? = null,
        val groupArtifact: String? = null,
        val style: String? = null,
        val reflowLongStrings: Boolean? = null,
        val reorderImports: Boolean? = null,
        val formatJavadoc: Boolean? = null,
    ) {

        fun applyTo(receiver: JavaExtension.GoogleJavaFormatConfig) {
            receiver::groupArtifact trySet groupArtifact
            receiver::style trySet style
            reflowLongStrings?.let(receiver::reflowLongStrings)
            receiver::reorderImports trySet reorderImports
            receiver::formatJavadoc trySet formatJavadoc
        }
    }

    @Serializable
    internal data class ImportOrderConfig(
        val importOrder: LinkedHashSet<String>? = null,
        val importOrderFile: String? = null,
        val wildcardsLast: Boolean? = null,
        val semanticSort: Boolean? = null,
        val treatAsPackage: Set<String>? = null,
        val treatAsClass: Set<String>? = null
    ) {

        fun applyTo(receiver: JavaExtension.ImportOrderConfig) {
            wildcardsLast?.let(receiver::wildcardsLast)
            semanticSort?.let(receiver::semanticSort)
            treatAsPackage?.let(receiver::treatAsPackage)
            treatAsClass?.let(receiver::treatAsClass)
        }
    }

    @Serializable
    internal data class PalantirJavaFormatConfig(
        val version: String? = null,
        val style: String? = null,
        val formatJavadoc: Boolean? = null
    ) {

        fun applyTo(receiver: JavaExtension.PalantirJavaFormatConfig) {
            receiver::style trySet style
            receiver::formatJavadoc trySet formatJavadoc
        }
    }
}
