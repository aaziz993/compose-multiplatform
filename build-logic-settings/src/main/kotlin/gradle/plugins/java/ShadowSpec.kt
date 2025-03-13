package gradle.plugins.java

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gradle.api.tasks.copy.CopySpec
import org.gradle.api.Project

internal interface ShadowSpec : CopySpec {

    val relocators: List<Relocator>?
    val configurations: List<List<String>>?
    val dependencyFilter: DependencyFilter?
    val enableRelocation: Boolean?
    val relocationPrefix: String?
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
    override fun applyTo(spec: org.gradle.api.file.CopySpec) {
        super.applyTo(spec)

        spec as ShadowJar

        relocators?.map(Relocator::toRelocator)?.forEach(spec::relocate)
        configurations?.map { files(*it.toTypedArray()) }?.let(spec::setConfigurations)

        dependencyFilter?.let { dependencyFilter ->
            spec.dependencies {
                dependencyFilter.applyTo(this)
            }
        }

        enableRelocation?.let(spec::setEnableRelocation)
        relocationPrefix?.let(spec::setRelocationPrefix)
        minimize?.takeIf { it }?.run { spec.minimize() }

        dependencyFilterForMinimize?.let { dependencyFilterForMinimize ->
            spec.minimize {
                dependencyFilterForMinimize.applyTo(this)
            }
        }

        mergeServiceFiles?.takeIf { it }?.run { spec.mergeServiceFiles() }
        mergeServiceFilesPath?.let(spec::mergeServiceFiles)
        append?.let(spec::append)
    }
}
