package gradle.plugins.kmp.web

import gradle.plugins.kmp.nat.KotlinNativeBinaryContainer
import gradle.serialization.serializer.DelegateTransformingSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.Project

@Serializable
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
internal data class KotlinJsBinaryContainer(
    private val delegate: Set<@Serializable(with = JsIrBinaryTransformingSerializer::class) JsIrBinary<*>> = mutableSetOf()
) : Set<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>> by delegate {

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer) {
        delegate.forEach { binary ->
            when (binary) {
                is Executable -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is ExecutableWasm -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is Library -> receiver.library(receiver.target.compilations.getByName(binary.compilation))
            }
        }
    }
}

internal object KotlinJsBinaryContainerTransformingSerializer
    : DelegateTransformingSerializer<KotlinJsBinaryContainer>(KotlinJsBinaryContainer.serializer())
