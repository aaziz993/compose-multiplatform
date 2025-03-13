package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.Named
import gradle.plugins.kotlin.HasKotlinDependencies
import gradle.plugins.kotlin.LanguageSettingsBuilder
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/**
 * Represents a logical group of Kotlin files, including sources, resources and additional metadata describing how
 * this group participates in the compilation of this project.
 *
 * For example, here's a common way to access all available within Kotlin source sets:
 * ```
 * kotlin.sourceSets.configureEach {
 *    // Here you can configure all Kotlin source sets.
 *    // For example, to add an additional source directory.
 *    kotlin.srcDir(project.layout.buildDirectory.dir("generatedSources"))
 * }
 * ```
 *
 * @see KotlinSourceSetContainer
 */
@Serializable
internal data class KotlinSourceSet(
    override val name: String = "",
    override val dependencies: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    /**
     * Provides the DSL to configure a subset of Kotlin compilation language settings for this [KotlinSourceSet].
     *
     * **Note**: The [LanguageSettingsBuilder] interface will be deprecated in the future.
     * Instead, it is better to use the existing `compilerOptions` DSL.
     */
    val languageSettings: LanguageSettingsBuilder? = null,

    /**
     * Contains a set of custom file extensions used to identify source files for this [KotlinSourceSet].
     *
     * These extensions are evaluated lazily and can include additional custom source file types beyond the default ".kt" and ".kts" ones.
     */
    val customSourceFilesExtensions: List<String>? = null,
) : Named, HasKotlinDependencies {

    context(Project)
    override fun applyTo(named: org.gradle.api.Named) {
        named as org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

        dependencies?.let { dependencies ->
            named.dependencies {
                dependencies.forEach { dependency ->
                    dependency.applyTo(this)
                }
            }
        }

        languageSettings?.applyTo(named.languageSettings)
        customSourceFilesExtensions?.let(named::addCustomSourceFilesExtensions)
    }

    context(Project)
    override fun applyTo() = applyTo(kotlin.sourceSets)
}

internal object KotlinSourceSetTransformingSerializer : KeyTransformingSerializer<KotlinSourceSet>(
    KotlinSourceSet.serializer(),
    "name",
)
