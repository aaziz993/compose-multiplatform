package plugin.project.kotlin.model.language.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.AbstractKotlinNativeBinaryContainer

@Serializable
internal data class Binaries(
    val framework: Framework?=null
){

    context(Project)
    fun applyTo(binaries: AbstractKotlinNativeBinaryContainer){
        framework?.let { framework ->
          binaries.framework {
                framework.applyTo(this)
            }
        }
    }
}
