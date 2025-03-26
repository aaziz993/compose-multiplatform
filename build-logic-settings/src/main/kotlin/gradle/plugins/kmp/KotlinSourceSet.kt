package gradle.plugins.kmp

import gradle.accessors.kotlin
import gradle.api.NamedKeyTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.file.SourceDirectorySet
import gradle.api.getByNameOrAll
import gradle.plugins.kotlin.HasKotlinDependencies
import gradle.plugins.kotlin.LanguageSettingsBuilder
import gradle.project.Dependency
import gradle.project.DependencyKeyTransformingSerializer
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
    override val name: String? = null,
    override val dependencies: Set<@Serializable(with = DependencyKeyTransformingSerializer::class) Dependency>? = null,
    /**
     * Represents a set of Kotlin source files that are included in this [KotlinSourceSet].
     *
     * @see SourceDirectorySet
     */
    val kotlin: SourceDirectorySet? = null,
    /**
     * Represents a set of resource files that are included in this [KotlinSourceSet].
     *
     * @see SourceDirectorySet
     */
    val resources: SourceDirectorySet? = null,
    /**
     * Provides the DSL to configure a subset of Kotlin compilation language settings for this [KotlinSourceSet].
     *
     * **Note**: The [LanguageSettingsBuilder] interface will be deprecated in the future.
     * Instead, it is better to use the existing `compilerOptions` DSL.
     */
    val languageSettings: LanguageSettingsBuilder? = null,
    /**
     * Returns a set of source sets that have a [dependsOn] relationship with this source set.
     *
     * @return a set of source sets added with `dependsOn` relationship at the current state of configuration.
     * Note that the Kotlin Gradle plugin may add additional required
     * source sets on late stages of Gradle configuration and the most reliable way to get a full final set is
     * to use this property as a task input with [org.gradle.api.provider.Provider] type.
     */
    val dependsOn: Set<String>? = null,
    /**
     * Contains a set of custom file extensions used to identify source files for this [KotlinSourceSet].
     *
     * These extensions are evaluated lazily and can include additional custom source file types beyond the default ".kt" and ".kts" ones.
     */
    val customSourceFilesExtensions: List<String>? = null,
) : ProjectNamed<org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet>,
    HasKotlinDependencies<org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet> {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet) {
        super.applyTo(receiver)

        kotlin?.applyTo(receiver.kotlin)
        resources?.applyTo(receiver.resources)
        languageSettings?.applyTo(receiver.languageSettings)
        dependsOn?.flatMap(project.kotlin.sourceSets::getByNameOrAll)?.forEach(receiver::dependsOn)
        customSourceFilesExtensions?.let(receiver::addCustomSourceFilesExtensions)
    }

    context(Project)
    fun applyTo() = applyTo(project.kotlin.sourceSets)
}

internal object KotlinSourceSetKeyTransformingSerializer : NamedKeyTransformingSerializer<KotlinSourceSet>(
    KotlinSourceSet.serializer(),
)
