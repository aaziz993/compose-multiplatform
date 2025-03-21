@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java.tasks.shadow

import gradle.api.tasks.copy.CopySpec
import gradle.plugins.java.Relocator
import gradle.plugins.java.tasks.DependencyFilter
import org.gradle.api.Project

internal interface ShadowSpec<T : ShadowSpec> : CopySpec<T> {

    val relocators: List<Relocator>?

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
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        relocators?.map(gradle.plugins.java.Relocator::toRelocator)?.forEach(recipient::relocate)

        dependencyFilter?.let { dependencyFilter ->
            recipient.dependencies {
                dependencyFilter.applyTo(this)
            }
        }

        minimize?.takeIf { it }?.run { recipient.minimize() }

        dependencyFilterForMinimize?.let { dependencyFilterForMinimize ->
            recipient.minimize {
                dependencyFilterForMinimize.applyTo(this)
            }
        }

        mergeServiceFiles?.takeIf { it }?.run { recipient.mergeServiceFiles() }
        mergeServiceFilesPath?.let(recipient::mergeServiceFiles)
        append?.let(recipient::append)
    }
}
