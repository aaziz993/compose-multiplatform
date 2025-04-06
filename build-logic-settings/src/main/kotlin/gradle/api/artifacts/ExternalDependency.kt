package gradle.api.artifacts

import klib.data.type.reflection.tryApply
import org.gradle.api.Project

/**
 *
 * An `ExternalDependency` is a [Dependency] on a source outside the current project hierarchy.
 */
internal interface ExternalDependency<T : org.gradle.api.artifacts.ExternalDependency> : ModuleDependency<T> {

    /**
     * Configures the version constraint for this dependency.
     * @param configureAction the configuration action for the module version
     * @since 4.4
     */
    val version: MutableVersionConstraint?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::version tryApply version?.let { version -> version::applyTo }
    }
}
