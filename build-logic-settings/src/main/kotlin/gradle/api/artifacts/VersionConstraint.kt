package gradle.api.artifacts

import gradle.api.Describable
import org.gradle.api.artifacts.VersionConstraint

/**
 * Represents a constraint that is used to match module versions to a dependency.
 * Each of [.getPreferredVersion], [.getRequiredVersion] and [.getStrictVersion] is represented by a version String,
 * that can be compared against a module version to determine if the version matches.
 *
 * <h2>Version syntax</h2>
 *
 *
 * Gradle supports different ways of declaring a version String:
 *
 *  * An exact version: e.g. 1.3, 1.3.0-beta3, 1.0-20150201.131010-1
 *  * A Maven-style version range: e.g. [1.0,), [1.1, 2.0), (1.2, 1.5]
 *
 *  * The '[' and ']' symbols indicate an inclusive bound; '(' and ')' indicate an exclusive bound.
 *  * When the upper or lower bound is missing, the range has no upper or lower bound.
 *  * The symbol ']' can be used instead of '(' for an exclusive lower bound, and '[' instead of ')' for exclusive upper bound. e.g "]1.0, 2.0["
 *
 *
 *  * A prefix version range: e.g. 1.+, 1.3.+
 *
 *  * Only versions exactly matching the portion before the '+' are included.
 *  * The range '+' on it's own will include any version.
 *
 *
 *  * A latest-status version: e.g. latest.integration, latest.release
 *
 *  * Will match the highest versioned module with the specified status. See [ComponentMetadata.getStatus].
 *
 *
 *  * A Maven SNAPSHOT version identifier: e.g. 1.0-SNAPSHOT, 1.4.9-beta1-SNAPSHOT
 *
 *
 * <h2>Version ordering</h2>
 *
 * Versions have an implicit ordering. Version ordering is used to:
 *
 *  * Determine if a particular version is included in a range.
 *  * Determine which version is 'newest' when performing conflict resolution.
 *
 *
 *
 * Versions are ordered based on the following rules:
 *
 *
 *  * Each version is split into it's constituent "parts":
 *
 *  * The characters [`. - _ +`] are used to separate the different "parts" of a version.
 *  * Any part that contains both digits and letters is split into separate parts for each: `1a1 == 1.a.1`
 *  * Only the parts of a version are compared. The actual separator characters are not significant: `1.a.1 == 1-a+1 == 1.a-1 == 1a1`
 *
 *
 *  * The equivalent parts of 2 versions are compared using the following rules:
 *
 *  * If both parts are numeric, the highest numeric value is **higher**: `1.1 &lt; 1.2`
 *  * If one part is numeric, it is considered **higher** than the non-numeric part: `1.a &lt; 1.1`
 *  * If both are not numeric, the parts are compared alphabetically, case-sensitive: `1.A &lt; 1.B  &lt; 1.a &lt; 1.b`
 *  * An version with an extra numeric part is considered **higher** than a version without: `1.1 &lt; 1.1.0`
 *  * An version with an extra non-numeric part is considered **lower** than a version without: `1.1.a &lt; 1.1`
 *
 *
 *  * Certain string values have special meaning for the purposes of ordering:
 *
 *  * The string "dev" is consider **lower** than any other string part: 1.0-dev &lt; 1.0-alpha &lt; 1.0-rc.
 *  * The strings "rc", "release" and "final" are considered **higher** than any other string part (sorted in that order): 1.0-zeta &lt; 1.0-rc &lt; 1.0-release &lt; 1.0-final &lt; 1.0.
 *  * The string "SNAPSHOT" **has no special meaning**, and is sorted alphabetically like any other string part: 1.0-alpha &lt; 1.0-SNAPSHOT &lt; 1.0-zeta &lt; 1.0-rc &lt; 1.0.
 *  * Numeric snapshot versions **have no special meaning**, and are sorted like any other numeric part: 1.0 &lt; 1.0-20150201.121010-123 &lt; 1.1.
 *
 *
 *
 *
 * @since 4.4
 */
internal interface VersionConstraint<T : VersionConstraint> : Describable<T> {

    /**
     * The branch to select versions from. When not `null` selects only those versions that were built from the specified branch.
     *
     * @since 4.6
     */
    val branch: String?

    /**
     * The required version of a module (which may be an exact version or a version range).
     *
     * The required version of a module can typically be upgraded during dependency resolution, but not downgraded.
     *
     * @return the required version, or empty string if no required version specified. Never null.
     */
    val require: String?

    /**
     * The preferred version of a module (which may be an exact version or a version range).
     *
     * The preferred version of a module provides a hint when resolving the version,
     * but will not be honored in the presence of conflicting constraints.
     *
     * @return the preferred version, or empty string if no preferred version specified. Never null.
     */
    val prefer: String?

    /**
     * The strictly required version of a module (which may be an exact version or a version range).
     *
     * The required version of a module is strictly enforced and cannot be upgraded or downgraded during dependency resolution.
     *
     * @return the strict version, or empty string if no required version specified. Never null.
     */
    val strictly: String?

    /**
     * Returns the list of versions that this module rejects  (which may be exact versions, or ranges, anything that fits into a version string).
     *
     * @return the list of rejected versions
     */
    val reject: List<String>?
}
