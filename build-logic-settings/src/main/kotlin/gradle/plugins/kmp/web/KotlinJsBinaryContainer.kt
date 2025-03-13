package gradle.plugins.kmp.web

import gradle.accessors.projectProperties
import gradle.project.ProjectType
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer

@Serializable
internal data class KotlinJsBinaryContainer(
    val executable: Executable = Executable(),
    val library: Library = Library(),
) {

    context(Project)
    fun applyTo(container: KotlinJsBinaryContainer) {
        when (projectProperties.type) {
            ProjectType.APP ->
                executable.let { binaries ->
                    binaries.compilation?.let { compilation ->
                        container.executable(container.target.compilations.getByName(compilation))
                    } ?: container.executable()
                }

            ProjectType.LIB -> library.let { binaries ->
                binaries.compilation?.let { compilation ->
                    container.library(container.target.compilations.getByName(compilation))
                } ?: container.library()
            }
        }
    }
}
