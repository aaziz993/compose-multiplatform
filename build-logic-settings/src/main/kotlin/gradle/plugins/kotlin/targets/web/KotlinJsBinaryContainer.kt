package gradle.plugins.kotlin.targets.web

import gradle.serialization.serializer.DelegateTransformingSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class)
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
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
    : DelegateTransformingSerializer<KotlinJsBinaryContainer>(KotlinJsBinaryContainer.generatedSerializer())
