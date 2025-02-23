package plugin.project.kotlin.model.language.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/

@Serializable
internal data class KotlinNativeBinaryContainer(
    val framework: Framework? = null,
) {

    context(Project)
    fun applyTo(binaries: KotlinNativeBinaryContainer) {
        framework?.let { framework ->
            binaries.framework {
                framework.applyTo(this)
            }
        }
    }
}
