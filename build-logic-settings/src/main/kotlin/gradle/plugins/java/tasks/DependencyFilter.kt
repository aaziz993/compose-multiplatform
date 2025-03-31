package gradle.plugins.java.tasks

import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import gradle.api.artifacts.ResolvedDependency
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DependencyFilter(
    /**
     * Resolve a FileCollection against the include/exclude rules in the filter
     * @param configuration
     * @return
     */
    val resolve: List<Set<String>>? = null,
    /**
     * Exclude dependencies that match the provided spec.
     *
     * @param spec
     * @return
     */
    val exclude: ResolvedDependency? = null,
    /**
     * Include dependencies that match the provided spec.
     *
     * @param spec
     * @return
     */
    val include: ResolvedDependency? = null,
) {

    context(Project)
    fun applyTo(receiver: DependencyFilter) {
        resolve?.map(Set<*>::toTypedArray)?.map(project::files)?.let(receiver::resolve)

        receiver.exclude {
            exclude?.equals(it) != false
        }

        receiver.include {
            include?.equals(it) != false
        }
    }
}
