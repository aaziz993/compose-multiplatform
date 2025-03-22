package gradle.plugins.kover.reports.filters

import gradle.api.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverReportFilter
import kotlinx.serialization.Serializable

/**
 * Exclusion or inclusion class filter for Kover reports.
 *
 * Exclusions example for Kotlin:
 * ```
 *     excludes {
 *          classes("*.foo.Bar", "*.M?Class")
 *          classes(listOf("*.foo.Bar", "*.M?Class"))
 *          packages("foo.b?r", "com.*.example")
 *          val somePackages =
 *          packages(listOf("foo.b?r", "com.*.example"))
 *          annotatedBy("*Generated*", "com.example.KoverExclude")
 *          projects.add(":my:lib*")
 *      }
 * ```
 */
@Serializable
internal data class KoverReportFilter(
    /**
     * Classes of current filter.
     *
     * It is acceptable to use `*` and `?` wildcards,
     * `*` means any number of arbitrary characters (including no chars), `?` means one arbitrary character.
     *
     * Example:
     * ```
     *  classes.addAll("*.foo.Bar", "*.M?Class")
     * ```
     */
    val classes: Set<String>? = null,
    /**
     * Filters for classes and functions marked by specified annotations.
     *
     * Use cases:
     *  - in case of exclusion filters all classes or function marked by at least one of the specified annotation will be excluded from the report
     *  - in case of inclusion filters only classes marked by at least one of the specified annotations will be included in the report
     *
     * It is acceptable to use `*` and `?` wildcards,
     * `*` means any number of arbitrary characters (including no chars), `?` means one arbitrary character.
     *
     * **_Does not work for JaCoCo_**
     *
     * Example:
     * ```
     *  annotatedBy.addAll("*Generated*", "com.example.KoverExclude")
     * ```
     */
    val annotatedBy: Set<String>? = null,
    /**
     * Add all classes in specified project. Only the project path is used (starts with a colon).
     *
     * It is acceptable to use `*` and `?` wildcards,
     * `*` means any number of arbitrary characters (including no chars), `?` means one arbitrary character.
     *
     * Example:
     * ```
     *  projects.add(":my:lib*")
     * ```
     */
    val projects: Set<String>? = null,
    /**
     * Filter classes extending at least one of the specified classes or implementing at least one of the interfaces.
     * The class itself with the specified name is not taken into account.
     *
     * The entire inheritance tree is analyzed; a class may inherit the specified class/interface indirectly and still be included in the report, unless the specified class/interface is located outside of the application (see below).
     *
     * The following classes and interfaces can be specified in arguments:
     *  - classes and interfaces declared in the application
     *  - classes and interfaces declared outside the application, however they are directly inherited or implemented by any type from the application
     *
     * Due to technical limitations, if a specified class or interface is not declared in the application and not extended/implemented directly by one of the application types, such a filter will have no effect.
     *
     * If this filter is specified, then the generation of the report may slow down, because it becomes necessary to analyze the inheritance tree.
     *
     * It is acceptable to use `*` and `?` wildcards,
     * `*` means any number of arbitrary characters (including no chars), `?` means one arbitrary character.
     *
     * **_Does not work for JaCoCo_**
     *
     * Example:
     * ```
     *  inheritedFrom.add("*Repository")
     * ```
     */
    val inheritedFrom: Set<String>? = null,
){

    fun applyTo(recipient: KoverReportFilter){
        recipient.classes tryAssign classes
        recipient.   annotatedBy tryAssign annotatedBy
        recipient. projects tryAssign projects
        recipient. inheritedFrom tryAssign inheritedFrom
    }
}
