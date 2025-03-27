@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.java.tasks.shadow

import gradle.ifTrue
import gradle.api.tasks.copy.CopySpec
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

        minimize?.ifTrue(receiver::minimize)

        dependencyFilterForMinimize?.let { dependencyFilterForMinimize ->
            receiver.minimize {
                dependencyFilterForMinimize.applyTo(this)
            }
        }

        mergeServiceFiles?.ifTrue(receiver::mergeServiceFiles)

        mergeServiceFilesPath?.let { mergeServiceFilesPath ->
            receiver.mergeServiceFiles(mergeServiceFilesPath)
        }

        append?.let { append ->
            receiver.append(append)
        }
    }
}
