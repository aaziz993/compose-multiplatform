package gradle.model.kotlin.kmp.web

import gradle.projectProperties
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer
import gradle.model.project.ProjectType

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

            else -> Unit
        }
    }
}
