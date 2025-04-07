package gradle.plugins.kotlin.targets.web

import gradle.accessors.moduleName
import gradle.plugins.kotlin.targets.web.tasks.BinaryenExec
import gradle.plugins.kotlin.targets.web.tasks.BinaryenExecImpl
import gradle.plugins.kotlin.targets.web.tasks.DefaultIncrementalSyncTaskImpl
import gradle.plugins.kotlin.targets.web.tasks.KotlinJsIrLink
import gradle.plugins.kotlin.targets.web.tasks.KotlinJsIrLinkImpl
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.ReflectionJsonObjectTransformingPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.ir.Executable
import org.jetbrains.kotlin.gradle.targets.js.ir.ExecutableWasm
import org.jetbrains.kotlin.gradle.targets.js.ir.Library
import org.jetbrains.kotlin.gradle.targets.js.ir.LibraryWasm

internal interface JsBinary<T : org.jetbrains.kotlin.gradle.targets.js.ir.JsBinary> {

    val compilation: String
    val name: String?
    val distribution: Distribution?

    context(Project)
    fun applyTo(receiver: T) {
        distribution?.applyTo(receiver.distribution, project.moduleName)
    }
}

@Serializable(with = ReflectionJsIrBinaryObjectTransformingPolymorphicSerializer::class)
internal sealed class JsIrBinary<T : org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary> : JsBinary<T> {
    abstract val generateTs: Boolean?

    abstract val linkTask: KotlinJsIrLink<out org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>?

    // Wasi target doesn't have sync task
    // need to extract wasm related binaries
    abstract val linkSyncTask: DefaultIncrementalSyncTaskImpl?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::generateTs trySet generateTs
        (linkTask as KotlinJsIrLink<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>?)
            ?.applyTo(receiver.linkTask.get())
        linkSyncTask?.applyTo(receiver.linkSyncTask.get())
    }
}

private class ReflectionJsIrBinaryObjectTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionJsonObjectTransformingPolymorphicSerializer<JsIrBinary<*>>(JsIrBinary::class)

internal interface WasmBinary<T : org.jetbrains.kotlin.gradle.targets.js.ir.WasmBinary> {

    val compilation: String?

    val name: String?

    val linkTask: KotlinJsIrLink<out org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>?

    val optimizeTask: BinaryenExec<out org.jetbrains.kotlin.gradle.targets.js.binaryen.BinaryenExec>?

    context(Project)
    fun applyTo(receiver: T) {
        (linkTask as KotlinJsIrLink<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink>?)
            ?.applyTo(receiver.linkTask.get())
        (optimizeTask as BinaryenExec<org.jetbrains.kotlin.gradle.targets.js.binaryen.BinaryenExec>?)
            ?.applyTo(receiver.optimizeTask.get())
    }
}

@Serializable
internal data class Executable(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
    override val linkTask: KotlinJsIrLinkImpl? = null,
    override val linkSyncTask: DefaultIncrementalSyncTaskImpl? = null,
) : JsIrBinary<Executable>()

@Serializable
internal data class ExecutableWasm(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
    override val linkTask: KotlinJsIrLinkImpl? = null,
    override val linkSyncTask: DefaultIncrementalSyncTaskImpl? = null,
    override val optimizeTask: BinaryenExecImpl? = null,
) : JsIrBinary<ExecutableWasm>(), WasmBinary<ExecutableWasm> {

    context(Project)
    override fun applyTo(receiver: ExecutableWasm) {
        super<JsIrBinary>.applyTo(receiver)
        super<WasmBinary>.applyTo(receiver)
    }
}

@Serializable
internal data class Library(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
    override val linkTask: KotlinJsIrLinkImpl? = null,
    override val linkSyncTask: DefaultIncrementalSyncTaskImpl? = null,
) : JsIrBinary<Library>()

@Serializable
internal data class LibraryWasm(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
    override val linkTask: KotlinJsIrLinkImpl? = null,
    override val linkSyncTask: DefaultIncrementalSyncTaskImpl? = null,
    override val optimizeTask: BinaryenExecImpl? = null,
) : JsIrBinary<LibraryWasm>(), WasmBinary<LibraryWasm> {

    context(Project)
    override fun applyTo(receiver: LibraryWasm) {
        super<JsIrBinary>.applyTo(receiver)
        super<WasmBinary>.applyTo(receiver)
    }
}
