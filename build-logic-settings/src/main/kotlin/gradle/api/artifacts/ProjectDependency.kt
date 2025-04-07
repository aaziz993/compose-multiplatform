package gradle.api.artifacts

/**
 *
 * A `ProjectDependency` is a [Dependency] on another project in the current project hierarchy.
 */
internal interface ProjectDependency<T : org.gradle.api.artifacts.ProjectDependency> : ModuleDependency<T> {

    /**
     * Get the path to the project that this dependency refers to relative to its owning build.
     *
     * @see Project.getPath
     * @since 8.11
     */
    val path: String
}
