package plugin.project.kotlin.model.language.nat

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

internal interface KotlinNativeTarget : HasBinaries<KotlinNativeBinaryContainer?> {

    val compilerOptions: KotlinNativeCompilerOptions?

    context(Project)
    fun applyTo(target: KotlinNativeTarget) {
        binaries?.let { binaries ->
            target.binaries {
                binaries.framework?.let { framework ->
                    framework {
                        framework.applyTo(this)
                    }
                }
            }
        }
    }
}
