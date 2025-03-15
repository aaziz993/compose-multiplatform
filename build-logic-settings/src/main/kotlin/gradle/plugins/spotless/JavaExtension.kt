package gradle.plugins.spotless

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.spotless.LineEnding
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.accessors.version
import gradle.accessors.versions
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
) : FormatExtension() {

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
                (googleJavaFormat.version?.resolveVersion() ?: settings.libs.versions.version("googleJavaFormat"))
                    ?.let(extension::googleJavaFormat) ?: extension.googleJavaFormat(),
            )
        }

        palantirJavaFormat?.let { palantirJavaFormat ->
            palantirJavaFormat.applyTo(
                (palantirJavaFormat.version?.resolveVersion() ?: settings.libs.versions.version("palantirJavaFormat"))
                    ?.let(extension::palantirJavaFormat) ?: extension.palantirJavaFormat(),
            )
        }

        eclipse?.let { eclipse ->
            eclipse.applyTo(
                (eclipse.formatterVersion?.resolveVersion() ?: settings.libs.versions.version("eclipseFormatter"))
                    ?.let(extension::eclipse) ?: extension.eclipse(),
            )
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

    @Serializable
    internal class CleanthatJavaConfig(
        val groupArtifact: String? = null,
        val version: String? = null,
        val sourceJdk: String? = null,
        val mutators: List<String?>? = null,
        val excludedMutators: List<String?>? = null,
        val includeDraft: Boolean? = null
    ){
        fun applyTo(format: JavaExtension.CleanthatJavaConfig){
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
        val settingsFiles: List<String>? = null,
        val p2Mirrors: Map<String, String>? = null
    ) {

        fun applyTo(eclipse: JavaExtension.EclipseConfig) {
            settingsFiles?.let { eclipse.configFile(*it.toTypedArray()) }
            p2Mirrors?.let(eclipse::withP2Mirrors)
        }
    }

    @Serializable
    internal data class FormatAnnotationsConfig(
        val addedTypeAnnotations: List<String>? = null,
        val removedTypeAnnotations: List<String>? = null
    ){
        fun applyTo(annotations: JavaExtension.FormatAnnotationsConfig){
            addedTypeAnnotations?.forEach(annotations::addTypeAnnotation)
            removedTypeAnnotations?.forEach(annotations::removeTypeAnnotation)
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

        fun applyTo(format: JavaExtension.GoogleJavaFormatConfig) {
            groupArtifact?.let(format::groupArtifact)
            style?.let(format::style)
            reflowLongStrings?.let(format::reflowLongStrings)
            reorderImports?.let(format::reorderImports)
            formatJavadoc?.let(format::formatJavadoc)
        }
    }

    @Serializable
    internal data class ImportOrderConfig(
        val importOrder: List<String>? = null,
        val importOrderFile: String? = null,
        val wildcardsLast: Boolean?=null,
        val semanticSort: Boolean?=null,
        val treatAsPackage: Set<String>? = null,
        val treatAsClass: Set<String>? = null
    ){
        fun applyTo(importOrder: JavaExtension.ImportOrderConfig){
            wildcardsLast?.let(importOrder::wildcardsLast)
            semanticSort?.let(importOrder::semanticSort)
            treatAsPackage?.let(importOrder::treatAsPackage)
            treatAsClass?.let(importOrder::treatAsClass)
        }
    }

    @Serializable
    internal data class PalantirJavaFormatConfig(
        val version: String? = null,
        var style: String? = null,
        var formatJavadoc: Boolean? = null
    ) {

        fun applyTo(format: JavaExtension.PalantirJavaFormatConfig) {
            style?.let(format::style)
            formatJavadoc?.let(format::formatJavadoc)
        }
    }
}
