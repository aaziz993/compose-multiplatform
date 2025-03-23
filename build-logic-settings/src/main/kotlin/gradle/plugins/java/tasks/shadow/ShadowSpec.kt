@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java.tasks.shadow

import gradle.api.tasks.copy.CopySpec
import gradle.plugins.java.tasks.shadow.Relocator
import gradle.plugins.java.tasks.DependencyFilter
import org.gradle.api.Project

internal interface ShadowSpec<T : com.github.jengelman.gradle.plugins.shadow.tasks.ShadowSpec> : CopySpec<T> {

    val relocators: Set<Relocator>?

    val dependencyFilter: DependencyFilter?

    val minimize: Boolean?

    val dependencyFilterForMinimize: DependencyFilter?

    /**
     * Syntactic sugar for merging service files in JARs.
     *
     * @return this
     */
    val mergeServiceFiles: Boolean?
    val mergeServiceFilesPath: String?

    /**
     * Syntax sugar for merging service files in JARs
     *
     * @return this
     */
    val append: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        relocators?.map(Relocator::toRelocator)?.forEach(receiver::relocate)

        dependencyFilter?.let { dependencyFilter ->
            receiver.dependencies {
                dependencyFilter.applyTo(this)
            }
        }

        minimize?.takeIf { it }?.run { receiver.minimize() }

        dependencyFilterForMinimize?.let { dependencyFilterForMinimize ->
            receiver.minimize {
                dependencyFilterForMinimize.applyTo(this)
            }
        }

        mergeServiceFiles?.takeIf { it }?.run { receiver.mergeServiceFiles() }
        mergeServiceFilesPath?.let(receiver::mergeServiceFiles)
        append?.let(receiver::append)
    }
}
