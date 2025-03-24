package gradle.plugins.apivalidation

import gradle.accessors.apiValidation
import gradle.api.trySet
import kotlinx.validation.ExperimentalBCVApi
import org.gradle.api.Project

internal interface ApiValidationExtension {

    /**
     * Disables API validation checks completely.
     */
    val validationDisabled: Boolean?

    /**
     * Fully qualified package names that are not consider  API.
     * For example, it could be `kotlinx.coroutines.internal` or `kotlinx.serialization.implementation`.
     */
    val ignoredPackages: Set<String>?
    val setIgnoredPackages: Set<String>?

    /**
     * Projects that are ignored by the API check.
     */
    val ignoredProjects: Set<String>?
    val setIgnoredProjects: Set<String>?

    /**
     * Fully qualified names of annotations that effectively exclude declarations from being .
     * Example of such annotation could be `kotlinx.coroutines.InternalCoroutinesApi`.
     */
    val nonPublicMarkers: Set<String>?
    val setNonPublicMarkers: Set<String>?

    /**
     * Fully qualified names of classes that are ignored by the API check.
     * Example of such a class could be `com.package.android.BuildConfig`.
     */
    val ignoredClasses: Set<String>?
    val setIgnoredClasses: Set<String>?

    /**
     * Fully qualified names of annotations that can be used to explicitly mark  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicMarkers: Set<String>?
    val setPublicMarkers: Set<String>?

    /**
     * Fully qualified package names that contain  declarations.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicPackages: Set<String>?
    val setPublicPackages: Set<String>?

    /**
     * Fully qualified names of  classes.
     * If at least one of [Markers], [Packages] or [Classes] is defined,
     * all declarations not covered by any of them will be considered non-.
     * [ignoredPackages], [ignoredClasses] and [nonMarkers] can be used for additional filtering.
     */
    val publicClasses: Set<String>?
    val setPublicClasses: Set<String>?

    /**
     * Non-default Gradle SourceSet names that should be validated.
     * By default, only the `main` source set is checked.
     */
    val additionalSourceSets: Set<String>?
    val setAdditionalSourceSets: Set<String>?

    /**
     * A path to a directory containing an API dump.
     * The path should be relative to the project's root directory and should resolve to its subdirectory.
     * By default, it's `api`.
     */
    val apiDumpDirectory: String?

    /**
     * KLib ABI validation settings.
     *
     * @see KlibValidationSettings
     */
    val klib: KlibValidationSettings?

    context(project: Project)
    @OptIn(ExperimentalBCVApi::class)
    fun applyTo() = project.pluginManager.withPlugin("binaryCompatibilityValidator") {
        project.apiValidation::validationDisabled trySet validationDisabled
        ignoredPackages?.let(project.apiValidation.ignoredPackages::addAll)
        project.apiValidation::ignoredPackages trySet setIgnoredPackages?.toMutableSet()
        ignoredProjects?.let(project.apiValidation.ignoredProjects::addAll)
        project.apiValidation::ignoredProjects trySet setIgnoredProjects?.toMutableSet()
        nonPublicMarkers?.let(project.apiValidation.nonPublicMarkers::addAll)
        project.apiValidation::nonPublicMarkers trySet setNonPublicMarkers?.toMutableSet()
        ignoredClasses?.let(project.apiValidation.ignoredClasses::addAll)
        project.apiValidation::ignoredClasses trySet setIgnoredClasses?.toMutableSet()
        publicMarkers?.let(project.apiValidation.publicMarkers::addAll)
        project.apiValidation::publicMarkers trySet setPublicMarkers?.toMutableSet()
        publicPackages?.let(project.apiValidation.publicPackages::addAll)
        project.apiValidation::publicPackages trySet setPublicPackages?.toMutableSet()
        publicClasses?.let(project.apiValidation.publicClasses::addAll)
        project.apiValidation::publicClasses trySet setPublicClasses?.toMutableSet()
        additionalSourceSets?.let(project.apiValidation.additionalSourceSets::addAll)
        project.apiValidation::additionalSourceSets trySet setAdditionalSourceSets?.toMutableSet()
        project.apiValidation::apiDumpDirectory trySet apiDumpDirectory
        klib?.applyTo(project.apiValidation.klib)
    }
}
