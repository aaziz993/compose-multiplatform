package gradle.api.artifacts

import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 * A `Dependency` represents a dependency on the artifacts from a particular source. A source can be an Ivy
 * module, a Maven POM, another Gradle project, a collection of Files, etc... A source can have zero or more artifacts.
 */
internal interface Dependency<T : Dependency> {

    /**
     * Sets the reason why this dependency should be used.
     *
     * @since 4.6
     */
    val because: String?

    context(Project)
    fun applyTo(receiver: T) {
        receiver::because trySet because
    }
}
