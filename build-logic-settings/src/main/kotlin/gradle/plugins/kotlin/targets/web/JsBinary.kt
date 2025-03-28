package gradle.plugins.kotlin.targets.web

import gradle.accessors.moduleName
import gradle.api.trySet
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.ir.Executable
import org.jetbrains.kotlin.gradle.targets.js.ir.ExecutableWasm

internal interface JsBinary<T : org.jetbrains.kotlin.gradle.targets.js.ir.JsBinary> {

    val compilation: String
    val name: String?
    val distribution: Distribution?

    context(Project)
    fun applyTo(receiver: T) {
        distribution?.applyTo(receiver.distribution, project.moduleName)
    }
}

@Serializable(with = JsIrBinarySerializer::class)
internal sealed class JsIrBinary<T : org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary> : JsBinary<T> {

    abstract val generateTs: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::generateTs trySet generateTs
    }
}

private object JsIrBinarySerializer : JsonContentPolymorphicSerializer<JsIrBinary<*>>(
    JsIrBinary::class,
)

internal object JsIrBinaryKeyTransformingSerializer : KeyTransformingSerializer<JsIrBinary<*>>(
    JsIrBinarySerializer,
    "type",
)

@Serializable
@SerialName("executable")
internal data class Executable(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
) : JsIrBinary<Executable>()

@Serializable
@SerialName("executableWasm")
internal data class ExecutableWasm(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
) : JsIrBinary<ExecutableWasm>()

@Serializable
@SerialName("library")
internal data class Library(
    override val compilation: String = KotlinCompilation.MAIN_COMPILATION_NAME,
    override val name: String? = null,
    override val distribution: Distribution? = null,
    override val generateTs: Boolean? = null,
) : JsIrBinary<Executable>()
