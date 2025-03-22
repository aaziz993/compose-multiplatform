package gradle.api.tasks

import gradle.api.ProjectNamed
import gradle.api.file.SourceDirectorySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

/**
 * A `SourceSet` represents a logical group of Java source and resource files. They
 * are covered in more detail in the
 * [user manual](https://docs.gradle.org/current/userguide/building_java_projects.html#sec:java_source_sets).
 *
 *
 * The following example shows how you can configure the 'main' source set, which in this
 * case involves excluding classes whose package begins 'some.unwanted.package' from
 * compilation of the source files in the 'java' [gradle.api.file.SourceDirectorySet]:
 *
 * <pre class='autoTested'>
 * plugins {
 * id 'java'
 * }
 *
 * sourceSets {
 * main {
 * java {
 * exclude 'some/unwanted/package/ **'
 * }
 * }
 * }
</pre> *
 */
@Serializable
internal data class SourceSet(
    /**
     * Returns the name of this source set.
     *
     * @return The name. Never returns null.
     */
    override val name: String? = null,,
    /**
     * Sets the classpath used to compile this source.
     *
     * @param classpath The classpath. Should not be null.
     */
    val compileClasspath: Set<String>? = null,
    /**
     * Set the classpath to use to load annotation processors when compiling this source set.
     * This path is also used for annotation processor discovery. The classpath can be empty,
     * which means use the compile classpath; if you want to disable annotation processing,
     * then use `-proc:none` as a compiler argument.
     *
     * @param annotationProcessorPath The annotation processor path. Should not be null.
     * @since 4.6
     */
    val annotationProcessorPath: Set<String>? = null,
    /**
     * Sets the classpath used to execute this source.
     *
     * @param classpath The classpath. Should not be null.
     */
    val runtimeClasspath: Set<String>? = null,
    /**
     * Registers a set of tasks which are responsible for compiling this source set into the classes directory. The
     * paths are evaluated as per [org.gradle.api.Task.dependsOn].
     *
     * @param taskPaths The tasks which compile this source set.
     * @return this
     */
    val compiledBy: Set<String>? = null,
    /**
     * Configures the non-Java resources for this set.
     *
     *
     * The given closure is used to configure the [gradle.api.file.SourceDirectorySet] which contains the resources.
     *
     * @param configureClosure The closure to use to configure the resources.
     * @return this
     */
    val resources: SourceDirectorySet? = null,
    /**
     * Returns the Java source which is to be compiled by the Java compiler into the class output directory.
     *
     * @return the Java source. Never returns null.
     */
    val java: SourceDirectorySet? = null,
    val allJava: SourceDirectorySet? = null,
    val allSource: SourceDirectorySet? = null,
) : ProjectNamed<SourceSet> {

    context(Project)
    override fun applyTo(recipient: SourceSet) {
        compileClasspath?.toTypedArray()?.let(recipient::compiledBy)
        annotationProcessorPath?.toTypedArray()?.let(::files)?.let(recipient::setAnnotationProcessorPath)
        runtimeClasspath?.toTypedArray()?.let(::files)?.let(recipient::setRuntimeClasspath)
        compiledBy?.toTypedArray()?.let(recipient::compiledBy)
        resources?.applyTo(recipient.resources)
        java?.applyTo(recipient.java)
        allJava?.applyTo(recipient.allJava)
        allSource?.applyTo(recipient.allSource)
    }
}
