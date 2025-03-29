package gradle.plugins.kotlin.targets.nat

import gradle.accessors.moduleName
import gradle.api.ProjectNamed
import gradle.api.tryAssign
import gradle.api.tryPlus
import gradle.api.tryPutAll
import gradle.api.trySet
import gradle.plugins.project.Dependency
import gradle.serialization.serializer.JsonContentPolymorphicSerializer
import gradle.serialization.serializer.JsonKeyValueTransformingSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

@Serializable
internal data class Binary(
    val namePrefix: String = "",
    val buildType: NativeBuildType,
)

internal object BinaryContentPolymorphicSerializer : kotlinx.serialization.json.JsonContentPolymorphicSerializer<Any>(
    Any::class,
) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer() else Binary.serializer()
}

@Serializable(with = NativeBinaryKeyValueTransformingSerializer::class)
internal sealed class NativeBinary<T : org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary> : ProjectNamed<T> {

    abstract val baseName: String?

    // Configuration DSL.
    abstract val debuggable: Boolean?

    abstract val optimized: Boolean?

    /** Additional options passed to the linker by the Kotlin/Native compiler. */
    abstract val linkerOpts: List<String>?

    abstract val setLinkerOpts: List<String>?

    abstract val binaryOptions: Map<String, String>?
    abstract val setBinaryOptions: Map<String, String>?

    /** Additional arguments passed to the Kotlin/Native compiler. */
    abstract val freeCompilerArgs: List<String>?

    abstract val setFreeCompilerArgs: List<String>?

    // Output access.
    // TODO: Provide output configurations and integrate them with Gradle Native.
    abstract val outputDirectory: String?

    abstract val outputDirectoryProperty: String?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver.baseName = baseName ?: project.moduleName
        receiver::debuggable trySet debuggable
        receiver::optimized trySet optimized
        linkerOpts?.let(receiver::linkerOpts)
        receiver.linkerOpts trySet setLinkerOpts
        receiver.binaryOptions tryPutAll binaryOptions
        receiver::binaryOptions trySet setBinaryOptions?.toMutableMap()
        receiver::freeCompilerArgs tryPlus freeCompilerArgs
        receiver::freeCompilerArgs trySet setFreeCompilerArgs
        receiver::outputDirectory trySet optimized?.let(project::file)
        receiver.outputDirectoryProperty tryAssign outputDirectoryProperty?.let(project.layout.projectDirectory::dir)
    }
}

private object NativeBinaryContentTransformingSerializer : JsonContentPolymorphicSerializer<NativeBinary<*>>(
    NativeBinary::class,
)

private object NativeBinaryKeyValueTransformingSerializer : JsonKeyValueTransformingSerializer<NativeBinary<*>>(
    NativeBinaryContentTransformingSerializer,
    "type",
)

@Serializable
internal data class NativeBinaryImpl(
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val name: String? = null,
) : NativeBinary<org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>()

internal abstract class AbstractExecutable<T : org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable> : NativeBinary<T>()

internal abstract class Executable : AbstractExecutable<org.jetbrains.kotlin.gradle.plugin.mpp.Executable>() {

    /**
     * The fully qualified name of the main function. For an example:
     *
     * - "main"
     * - "com.example.main"
     *
     *  The main function can either take no arguments or an Array<String>.
     */
    abstract val entryPoint: String?

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.Executable) {
        super.applyTo(receiver)

        entryPoint?.let(receiver::entryPoint)
    }
}

@Serializable
@SerialName("executable")
internal data class ExecutableSettings(
    override val name: String? = null,
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val entryPoint: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : Executable()

internal abstract class TestExecutable : NativeBinary<org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable>()

@Serializable
@SerialName("testExecutable")
internal data class TestExecutableSettings(
    override val name: String? = null,
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : TestExecutable()

internal abstract class AbstractNativeLibrary<T : org.jetbrains.kotlin.gradle.plugin.mpp.AbstractNativeLibrary>
    : NativeBinary<T>() {

    /**
     * If dependencies added by the [export] method are resolved transitively or not.
     */
    abstract val transitiveExport: Boolean?

    /**
     * Add a dependency to be exported in the framework.
     */
    abstract val exports: Set<Dependency>?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::transitiveExport trySet transitiveExport
        exports?.forEach { export ->
            receiver.export(export.resolve())
        }
    }
}

internal abstract class StaticLibrary : AbstractNativeLibrary<org.jetbrains.kotlin.gradle.plugin.mpp.StaticLibrary>()

@Serializable
@SerialName("staticLibrary")
internal data class StaticLibrarySettings(
    override val transitiveExport: Boolean? = null,
    override val exports: Set<Dependency>? = null,
    override val baseName: String? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val name: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : StaticLibrary()

internal abstract class SharedLibrary : AbstractNativeLibrary<org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary>()

@Serializable
@SerialName("sharedLibrary")
internal data class SharedLibrarySettings(
    override val name: String? = null,
    override val baseName: String? = null,
    override val transitiveExport: Boolean? = null,
    override val exports: Set<Dependency>? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : SharedLibrary()

internal abstract class Framework : AbstractNativeLibrary<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>() {

    abstract val isStatic: Boolean?

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.Framework) {
        super.applyTo(receiver)

        receiver::isStatic trySet isStatic
    }
}

@Serializable
@SerialName("framework")
internal data class FrameworkSettings(
    override val name: String? = null,
    override val baseName: String? = null,
    override val transitiveExport: Boolean? = null,
    override val exports: Set<Dependency>? = null,
    override val debuggable: Boolean? = null,
    override val optimized: Boolean? = null,
    override val linkerOpts: List<String>? = null,
    override val setLinkerOpts: List<String>? = null,
    override val binaryOptions: Map<String, String>? = null,
    override val setBinaryOptions: Map<String, String>? = null,
    override val freeCompilerArgs: List<String>? = null,
    override val setFreeCompilerArgs: List<String>? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val isStatic: Boolean? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : Framework()
