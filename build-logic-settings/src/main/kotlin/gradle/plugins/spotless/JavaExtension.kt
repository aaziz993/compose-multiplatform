package gradle.plugins.spotless

import com.diffplug.gradle.spotless.JavaExtension
import com.diffplug.spotless.LineEnding
import gradle.accessors.libs
import gradle.accessors.resolveVersion
import gradle.accessors.settings
import gradle.accessors.spotless
import gradle.accessors.version
import gradle.accessors.versions
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project

@Serializable
@SerialName("java")
internal data class JavaExtension(
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
    val importOrder: ImportOrderConfig? = null,
    val removeUnusedImports: @Serializable(with = RemoveUnusedImportsContentPolymorphicSerializer::class) Any? = null,
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
    override fun applyTo(extension: JavaExtension) {
        super.applyTo(extension)

        importOrder?.let { importOrder ->
            importOrder.applyTo(
                importOrder.importOrder?.let { extension.importOrder(*it.toTypedArray()) }
                    ?: extension.importOrderFile(importOrder.importOrderFile!!),
            )
        }

        when (removeUnusedImports) {
            is Boolean -> removeUnusedImports.takeIf { it }?.let { extension.removeUnusedImports() }
            is String -> extension.removeUnusedImports(removeUnusedImports)
            else -> Unit
        }

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

        formatAnnotations?.applyTo(extension.formatAnnotations())
        cleanthat?.applyTo(extension.cleanthat())
    }

    context(Project)
    override fun applyTo() = spotless.java {
        applyTo(this)
    }

    private object RemoveUnusedImportsContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
            if (element.jsonPrimitive.isString)
                String.serializer()
            else Boolean.serializer()
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
            configFiles?.toTypedArray()?.let(eclipse::configFile)
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
            groupArtifact?.let(receiver::groupArtifact)
            style?.let(receiver::style)
            reflowLongStrings?.let(receiver::reflowLongStrings)
            reorderImports?.let(receiver::reorderImports)
            formatJavadoc?.let(receiver::formatJavadoc)
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

    private object ImportOrderContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> {
            TODO("Not yet implemented")
        }
    }

    @Serializable
    internal data class PalantirJavaFormatConfig(
        val version: String? = null,
        val style: String? = null,
        val formatJavadoc: Boolean? = null
    ) {

        fun applyTo(receiver: JavaExtension.PalantirJavaFormatConfig) {
            style?.let(receiver::style)
            formatJavadoc?.let(receiver::formatJavadoc)
        }
    }
}
