package plugin.project.kotlinnative.model

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.AbstractKotlinNativeBinaryContainer
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinTargetWithBinaries

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
