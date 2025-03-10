package gradle.model.gradle.publish.repository

import gradle.model.gradle.publish.repository.Module
import gradle.model.gradle.publish.repository.Version
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor

/**
 *
 * Descriptor of a repository content, used to avoid reaching to
 * an external repository when not needed.
 *
 *
 * Excludes are applied after includes. This means that by default, everything is included and nothing excluded.
 * If includes are added, then if the module doesn't match any of the includes, it's excluded. Then if it does, but
 * it also matches one of the excludes, it's also excluded.
 *
 * @since 5.1
 */

internal interface RepositoryContentDescriptor : InclusiveRepositoryContentDescriptor {

    /**
     * Declares that an entire group shouldn't be searched for in this repository.
     *
     * @param group the group name
     */
    val excludeGroups: List<String>?

    /**
     * Declares that an entire group and its subgroups shouldn't be searched for in this repository.
     *
     *
     *
     * A subgroup is a group that starts with the given prefix and has a dot immediately after the prefix.
     * For example, if the prefix is `org.gradle`, then `org.gradle` is matched as a group,
     * and `org.gradle.foo` and `org.gradle.foo.bar` are matched as subgroups. `org.gradlefoo`
     * is not matched as a subgroup.
     *
     *
     * @param groupPrefix the group prefix to include
     * @since 8.1
     */
    val excludeGroupsAndSubgroups: List<String>?

    /**
     * Declares that an entire group shouldn't be searched for in this repository.
     *
     * @param groupRegex the group name regular expression
     */
    val excludeGroupByRegexes: List<String>?

    /**
     * Declares that an entire module shouldn't be searched for in this repository.
     *
     * @param group the group name
     * @param moduleName the module name
     */
    val excludeModules: List<Module>?

    /**
     * Declares that an entire module shouldn't be searched for in this repository, using regular expressions.
     *
     * @param groupRegex the group name regular expression
     * @param moduleNameRegex the module name regular expression
     */
    val excludeModulesByRegexes: List<Module>?

    /**
     * Declares that a specific module version shouldn't be searched for in this repository.
     *
     *
     * The version notation for a regex will be matched against a single version and does not support range notations.
     *
     * @param group the group name
     * @param moduleName the module name
     * @param version the module version
     */
    val excludeVersions: List<Version>?

    /**
     * Declares that a specific module version shouldn't be searched for in this repository, using regular expressions.
     *
     *
     * The version notation for a regex will be matched against a single version and does not support range notations.
     *
     * @param groupRegex the group name
     * @param moduleNameRegex the module name
     * @param versionRegex the module version
     */
    val excludeVersionsByRegexes: List<Version>?

    /**
     * Declares that this repository should not be used for a specific
     * set of configurations. Defaults to any configuration
     *
     * @param configurationNames the names of the configurations the repository will not be used for
     */
    val notForConfigurations: List<String>?

    @Suppress("UnstableApiUsage")
    override fun applyTo(descriptor: org.gradle.api.artifacts.repositories.InclusiveRepositoryContentDescriptor) {
        super.applyTo(descriptor)

        descriptor as RepositoryContentDescriptor

        excludeGroups?.forEach(descriptor::excludeGroup)
        excludeGroupsAndSubgroups?.forEach(descriptor::excludeGroupAndSubgroups)
        excludeGroupByRegexes?.forEach(descriptor::excludeGroupByRegex)

        excludeModules?.forEach { (group, moduleName) ->
            descriptor.excludeModule(group, moduleName)
        }

        excludeModulesByRegexes?.forEach { (group, moduleName) ->
            descriptor.excludeModuleByRegex(group, moduleName)
        }

        excludeVersions?.forEach { (group, moduleName, version) ->
            descriptor.excludeVersion(group, moduleName, version)
        }

        excludeVersionsByRegexes?.forEach { (group, moduleName, version) ->
            descriptor.excludeVersionByRegex(group, moduleName, version)
        }

        notForConfigurations?.let { notForConfigurations ->
            descriptor.notForConfigurations(*notForConfigurations.toTypedArray())
        }
    }
}
