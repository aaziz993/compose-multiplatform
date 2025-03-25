package gradle.plugins.kmp.web

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer

@Serializable
internal data class KotlinJsBinaryContainer(
    @Transient
    private val binaries: Set<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>> = mutableSetOf()
) : Set<@Serializable(with = JsBinaryTransformingSerializer::class) JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>> by binaries {

    context(Project)
    fun applyTo(receiver: KotlinJsBinaryContainer) {
        binaries.forEach { binary ->
            when (binary) {
                is Executable -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is ExecutableWasm -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is Library -> receiver.library(receiver.target.compilations.getByName(binary.compilation))
            }
        }
    }
}
