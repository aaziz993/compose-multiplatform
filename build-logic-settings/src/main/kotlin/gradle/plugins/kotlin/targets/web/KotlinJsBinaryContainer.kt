package gradle.plugins.kotlin.targets.web

import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@KeepGeneratedSerializer
@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class)
internal data class KotlinJsBinaryContainer(
    private val delegate: Set<JsIrBinary<out @Contextual org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>> = mutableSetOf()
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

private object KotlinJsBinaryContainerTransformingSerializer
    : JsonObjectTransformingSerializer<KotlinJsBinaryContainer>(
    KotlinJsBinaryContainer.generatedSerializer(),
    "delegate",
)
