package gradle.api.tasks.compile

import gradle.accessors.files
import gradle.api.provider.tryAssign
import gradle.api.tasks.SourceTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

/**
 * The base class for all JVM-based language compilation tasks.
 */
internal abstract class AbstractCompile<T : org.gradle.api.tasks.compile.AbstractCompile> : SourceTask<T>() {

    /**
     * Sets the directory property that represents the directory to generate the `.class` files into.
     *
     * @return The destination directory property.
     * @since 6.1
     */
    abstract val destinationDirectory: String?

    /**
     * Sets the classpath to use to compile the source files.
     *
     * @return The classpath.
     */
    abstract val classpath: Set<String>?

    /**
     * Sets the Java language level to use to compile the source files.
     *
     * @return The source language level.
     */
    abstract val sourceCompatibility: String?

    /**
     * Sets the target JVM to generate the `.class` files for.
     *
     * @return The target JVM.
     */
    abstract val targetCompatibility: String?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver.destinationDirectory tryAssign destinationDirectory?.let(project.layout.projectDirectory::dir)
        receiver::setClasspath trySet classpath?.let(project::files)
        receiver::setSourceCompatibility trySet sourceCompatibility
        receiver::setTargetCompatibility trySet targetCompatibility
    }
}

@Serializable
@SerialName("AbstractCompile")
internal data class AbstractCompileImpl(
    override val destinationDirectory: String? = null,
    override val classpath: Set<String>? = null,
    override val sourceCompatibility: String? = null,
    override val targetCompatibility: String? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
    override val sourceFiles: Set<String>? = null,
    override val setSourceFiles: Set<String>? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
) : AbstractCompile<org.gradle.api.tasks.compile.AbstractCompile>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.compile.AbstractCompile>())
}
