package gradle.plugins.java

import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class DependencyFilter(

    /**
     * Resolve a FileCollection against the include/exclude rules in the filter
     * @param configuration
     * @return
     */
    val resolve: List<List<String>>? = null,
) {

    context(Project)
    fun applyTo(recipient: DependencyFilter) {
        resolve?.map { files(*it.toTypedArray()) }?.let(filter::resolve)
    }
}
