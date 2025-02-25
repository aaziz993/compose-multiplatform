package plugin.project.kotlin.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import plugin.model.dependency.ProjectDependency
import plugin.model.dependency.StandardDependency
import plugin.project.kotlin.model.language.LanguageSettingsBuilder

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
    override val dependencies: List<ProjectDependency>? = null,
    /**
     * Provides the DSL to configure a subset of Kotlin compilation language settings for this [KotlinSourceSet].
     *
     * **Note**: The [plugin.project.kotlin.model.language.LanguageSettingsBuilder] interface will be deprecated in the future.
     * Instead, it is better to use the existing `compilerOptions` DSL.
     */
    val languageSettings: LanguageSettingsBuilder? = null,

    /**
     * Contains a set of custom file extensions used to identify source files for this [KotlinSourceSet].
     *
     * These extensions are evaluated lazily and can include additional custom source file types beyond the default ".kt" and ".kts" ones.
     */
    val customSourceFilesExtensions: List<String>? = null
) : HasKotlinDependencies {

    context(Project)
    fun applyTo(sourceSet: KotlinSourceSet) {
        sourceSet.dependencies {
            dependencies?.filterIsInstance<StandardDependency>()?.forEach { dependency -> dependency.applyTo(this) }
        }

        languageSettings?.applyTo(sourceSet.languageSettings)
        customSourceFilesExtensions?.let(sourceSet::addCustomSourceFilesExtensions)
    }
}
