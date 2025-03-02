package plugin.project.gradle.model

import gradle.tryAssign
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.compile.AbstractCompile
import plugin.project.kotlin.model.Task
import plugin.project.kotlin.model.configure

/**
 * The base class for all JVM-based language compilation tasks.
 */

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
    override fun applyTo(_tasks: NamedDomainObjectContainer<org.gradle.api.Task>) {
        super.applyTo(_tasks)

        _tasks.configure {
            this as AbstractCompile

            destinationDirectory tryAssign this@AbstractCompile.destinationDirectory?.let(layout.projectDirectory::dir)

            this@AbstractCompile.classpath?.let { classpath ->
                setClasspath(files(*classpath.toTypedArray()))
            }

            this@AbstractCompile.sourceCompatibility?.let(::setSourceCompatibility)
            this@AbstractCompile.targetCompatibility?.let(::setTargetCompatibility)
        }
    }
}
