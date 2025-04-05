package gradle.plugins.kotlin.targets.web

import klib.data.type.serialization.serializer.SetSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import org.gradle.api.Project

@Serializable(with = KotlinJsBinaryContainerSetSerializer::class)
internal class KotlinJsBinaryContainer : LinkedHashSet<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>() {

    context(Project)
    fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsBinaryContainer) {
        forEach { binary ->
            binary as JsIrBinary<org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>
            when (binary) {
                is Executable, is ExecutableWasm -> receiver.executable(receiver.target.compilations.getByName(binary.compilation))
                is Library, is LibraryWasm -> receiver.library(receiver.target.compilations.getByName(binary.compilation))
            }.forEach {
                binary.applyTo(it)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
private object KotlinJsBinaryContainerSetSerializer
    : KSerializer<KotlinJsBinaryContainer> by SetSerializer(
    JsIrBinary.serializer(NothingSerializer()) as KSerializer<JsIrBinary<out org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>>,
    ::KotlinJsBinaryContainer,
)
