package gradle.plugins.apivalidation

import gradle.accessors.apiValidation
import gradle.collection.tryAddAll
import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import kotlinx.validation.ExperimentalBCVApi
import org.gradle.api.Project

@Serializable
internal data class ApiValidationExtension(
    /**
     * Disables API validation checks completely.
     */
    val validationDisabled: Boolean? = null,
    /**
     * Fully qualified package names that are not consider  API.
     * For example, it could be `kotlinx.coroutines.internal` or `kotlinx.serialization.implementation`.
     */
    val ignoredPackages: Set<String>? = null,
    val setIgnoredPackages: Set<String>? = null,
    /**
     * Projects that are ignored by the API check.
     */
    val ignoredProjects: Set<String>? = null,
    val setIgnoredProjects: Set<String>? = null,
    /**
     * Fully qualified names of annotations that effectively exclude declarations from being .
     * Example of such annotation could be `kotlinx.coroutines.InternalCoroutinesApi`.
     */
    val nonPublicMarkers: Set<String>? = null,
    val setNonPublicMarkers: Set<String>? = null,
    /**
     * Fully qualified names of classes that are ignored by the API check.
     * Example of such a class could be `com.package.android.BuildConfig`.
     */
    val ignoredClasses: Set<String>? = null,
    val setIgnoredClasses: Set<String>? = null,
    /**
     * Fully qualified names of annotations that can be used to explicitly mark  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicMarkers: Set<String>? = null,
    val setPublicMarkers: Set<String>? = null,
    /**
     * Fully qualified package names that contain  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicPackages: Set<String>? = null,
    val setPublicPackages: Set<String>? = null,
    /**
     * Fully qualified names of  classes.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicClasses: Set<String>? = null,
    val setPublicClasses: Set<String>? = null,
    /**
     * Non-default Gradle SourceSet names that should be validated.
     * By default, only the `main` source set is checked.
     */
    val additionalSourceSets: Set<String>? = null,
    val setAdditionalSourceSets: Set<String>? = null,
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
) {

    context(Project)
    @OptIn(ExperimentalBCVApi::class)
    fun applyTo() = project.pluginManager.withPlugin("org.jetbrains.kotlinx.binary-compatibility-validator") {
        project.apiValidation::validationDisabled trySet validationDisabled
        project.apiValidation.ignoredPackages tryAddAll ignoredPackages
        project.apiValidation::ignoredPackages trySet setIgnoredPackages?.toMutableSet()
        project.apiValidation.ignoredProjects tryAddAll ignoredProjects
        project.apiValidation::ignoredProjects trySet setIgnoredProjects?.toMutableSet()
        project.apiValidation.nonPublicMarkers tryAddAll nonPublicMarkers
        project.apiValidation::nonPublicMarkers trySet setNonPublicMarkers?.toMutableSet()
        project.apiValidation.ignoredClasses tryAddAll ignoredClasses
        project.apiValidation::ignoredClasses trySet setIgnoredClasses?.toMutableSet()
        project.apiValidation.publicMarkers tryAddAll publicMarkers
        project.apiValidation::publicMarkers trySet setPublicMarkers?.toMutableSet()
        project.apiValidation.publicPackages tryAddAll publicPackages
        project.apiValidation::publicPackages trySet setPublicPackages?.toMutableSet()
        project.apiValidation.publicClasses tryAddAll publicClasses
        project.apiValidation::publicClasses trySet setPublicClasses?.toMutableSet()
        project.apiValidation.additionalSourceSets tryAddAll additionalSourceSets
        project.apiValidation::additionalSourceSets trySet setAdditionalSourceSets?.toMutableSet()
        project.apiValidation::apiDumpDirectory trySet apiDumpDirectory
        klib?.applyTo(project.apiValidation.klib)
    }
}
