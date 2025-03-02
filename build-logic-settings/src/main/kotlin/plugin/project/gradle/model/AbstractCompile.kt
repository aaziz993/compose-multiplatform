package plugin.project.gradle.model

import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import plugin.project.kotlin.model.Task

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
    override fun applyTo(task: org.gradle.api.Task) {
        super.applyTo(task)

        task as AbstractCompile

        task.destinationDirectory tryAssign destinationDirectory?.let(layout.projectDirectory::dir)

        classpath?.let { classpath ->
            task.classpath = files(*classpath.toTypedArray())
        }

        sourceCompatibility?.let(task::setSourceCompatibility)
        targetCompatibility?.let(task::setTargetCompatibility)
    }
}
