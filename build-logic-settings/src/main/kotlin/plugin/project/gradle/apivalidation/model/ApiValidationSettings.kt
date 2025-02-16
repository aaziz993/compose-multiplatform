package plugin.project.gradle.apivalidation.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiValidationSettings(
    /**
     * Disables API validation checks completely.
     */
    val validationDisabled: Boolean? = null,
    /**
     * Fully qualified package names that are not consider  API.
     * For example, it could be `kotlinx.coroutines.internal` or `kotlinx.serialization.implementation`.
     */
    val ignoredPackages: Set<String>? = null,
    /**
     * Projects that are ignored by the API check.
     */
    val ignoredProjects: Set<String>? = null,
    /**
     * Fully qualified names of annotations that effectively exclude declarations from being .
     * Example of such annotation could be `kotlinx.coroutines.InternalCoroutinesApi`.
     */
    val nonMarkers: Set<String>? = null,
    /**
     * Fully qualified names of classes that are ignored by the API check.
     * Example of such a class could be `com.package.android.BuildConfig`.
     */
    val ignoredClasses: Set<String>? = null,
    /**
     * Fully qualified names of annotations that can be used to explicitly mark  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val Markers: Set<String>? = null,
    /**
     * Fully qualified package names that contain  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val Packages: Set<String>? = null,
    /**
     * Fully qualified names of  classes.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val Classes: Set<String>? = null,
    /**
     * Non-default Gradle SourceSet names that should be validated.
     * By default, only the `main` source set is checked.
     */
    val additionalSourceSets: Set<String>? = null,
    /**
     * A path to a directory containing an API dump.
     * The path should be relative to the project's root directory and should resolve to its subdirectory.
     * By default, it's `api`.
     */
    val apiDumpDirectory: String? = null,
    /**
     * KLib ABI validation settings.
     *
     * @see KlibValidationSettings
     */
    val klib: KlibValidationSettings? = null,
)
