package gradle.api.repositories

/**
 *
 * Descriptor of a repository content, used to avoid reaching to
 * an external repository when not needed.
 *
 *
 * Only inclusions can be defined at this level (cross-repository content).
 * Excludes need to be defined per-repository using [RepositoryContentDescriptor].
 * Similarly to declaring includes on specific repositories via [ArtifactRepository.content],
 * inclusions are *extensive*, meaning that anything which doesn't match the includes will be
 * considered missing from the repository.
 *
 *
 * @since 6.2
 */
internal interface InclusiveRepositoryContentDescriptor {

    /**
     * Declares that an entire group should be searched for in this repository.
     *
     * @param group the group name
     */
    val includeGroups: List<String>?

    /**
     * Declares that an entire group and its subgroups should be searched for in this repository.
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
    val includeGroupsAndSubgroups: List<String>?

    /**
     * Declares that an entire group should be searched for in this repository.
     *
     * @param groupRegex a regular expression of the group name
     */
    val includeGroupsByRegexes: List<String>?

    /**
     * Declares that an entire module should be searched for in this repository.
     *
     * @param group the group name
     * @param moduleName the module name
     */
    val includeModules: List<Module>?

    /**
     * Declares that an entire module should be searched for in this repository, using regular expressions.
     *
     * @param groupRegex the group name regular expression
     * @param moduleNameRegex the module name regular expression
     */
    val includeModulesByRegexes: List<Module>?

    /**
     * Declares that a specific module version should be searched for in this repository.
     *
     * @param group the group name
     * @param moduleName the module name
     * @param version the module version
     */
    val includeVersions: List<Version>?

    /**
     * Declares that a specific module version should be searched for in this repository, using regular expressions.
     *
     * @param groupRegex the group name regular expression
     * @param moduleNameRegex the module name regular expression
     * @param versionRegex the module version regular expression
     */
    val includeVersionsByRegexes: List<Version>?

    @Suppress("UnstableApiUsage")
    fun applyTo(descriptor: org.gradle.api.artifacts.repositories.InclusiveRepositoryContentDescriptor) {
        includeGroups?.forEach(descriptor::includeGroup)
        includeGroupsAndSubgroups?.forEach(descriptor::includeGroupAndSubgroups)
        includeGroupsByRegexes?.forEach(descriptor::includeGroupByRegex)

        includeModules?.forEach { (group, moduleName) ->
            descriptor.includeModule(group, moduleName)
        }

        includeModulesByRegexes?.forEach { (group, moduleName) ->
            descriptor.includeModuleByRegex(group, moduleName)
        }

        includeVersions?.forEach { (group, moduleName, version) ->
            descriptor.includeVersion(group, moduleName, version)
        }

        includeVersionsByRegexes?.forEach { (group, moduleName, version) ->
            descriptor.includeVersionByRegex(group, moduleName, version)
        }
    }
}

