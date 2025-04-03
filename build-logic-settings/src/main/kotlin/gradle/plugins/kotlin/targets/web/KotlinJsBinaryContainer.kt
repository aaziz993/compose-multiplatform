package gradle.plugins.kotlin.targets.web

import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import klib.data.type.serialization.serializer.NotSerializable
import klib.data.type.serialization.serializer.NothingSerializer
import klib.data.type.serialization.serializer.SetSerializer
import klib.data.type.serialization.serializer.ignoreTypeParameters
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.gradle.api.Project

@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@KeepGeneratedSerializer
@Serializable(with = KotlinJsBinaryContainerTransformingSerializer::class)
internal class KotlinJsBinaryContainer
    : HashSet<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>() {

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer) {
        forEach { binary ->
            when (binary) {
                is Executable -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is ExecutableWasm -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is Library -> receiver.library(receiver.target.compilations.getByName(binary.compilation))
            }
        }
    }
}

private object KotlinJsBinaryContainerTransformingSerializer
    : SetSerializer<KotlinJsBinaryContainer, JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>(
    JsIrBinary.serializer(org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary::class.serializer()) as KSerializer<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>,
)
