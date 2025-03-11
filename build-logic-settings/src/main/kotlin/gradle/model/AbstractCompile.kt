package gradle.model

import gradle.serialization.serializer.AnySerializer
import gradle.tryAssign
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * The base class for all JVM-based language compilation tasks.
 */
@Serializable
internal abstract class AbstractCompile : Task {

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
    abstract val classpath: List<String>?

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
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.gradle.api.tasks.compile.AbstractCompile

        named.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)

        classpath?.let { classpath ->
            named.classpath = files(*classpath.toTypedArray())
        }

        sourceCompatibility?.let(named::setSourceCompatibility)
        targetCompatibility?.let(named::setTargetCompatibility)
    }
}

@Serializable
@SerialName("AbstractCompile")
internal data class AbstractCompileImpl(
    override val destinationDirectory: String? = null,
    override val classpath: List<String>? = null,
    override val sourceCompatibility: String? = null,
    override val targetCompatibility: String? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
) : AbstractCompile(){

    override val name: String
        get() = ""
}
