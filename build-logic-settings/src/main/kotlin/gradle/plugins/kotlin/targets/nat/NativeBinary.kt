package gradle.plugins.kotlin.targets.nat

import gradle.accessors.moduleName
import gradle.api.ProjectNamed
import gradle.api.artifacts.Dependency
import gradle.api.provider.tryAssign
import gradle.process.AbstractExecTask
import gradle.process.AbstractExecTaskImpl
import klib.data.type.collection.tryPutAll
import klib.data.type.collection.trySet
import klib.data.type.reflection.tryPlus
import klib.data.type.reflection.trySet
import klib.data.type.serialization.json.serializer.ReflectionJsonObjectTransformingPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
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

@Serializable(with = ReflectionNativeBinaryObjectTransformingPolymorphicSerializer::class)
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

    abstract val linkTaskProvider: KotlinNativeLink<out org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>?

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
        (linkTaskProvider as KotlinNativeLink<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>?)
            ?.applyTo(receiver.linkTaskProvider.get())
        receiver::outputDirectory trySet optimized?.let(project::file)
        receiver.outputDirectoryProperty tryAssign outputDirectoryProperty?.let(project.layout.projectDirectory::dir)
    }
}

private class ReflectionNativeBinaryObjectTransformingPolymorphicSerializer(serializer: KSerializer<Nothing>)
    : ReflectionJsonObjectTransformingPolymorphicSerializer<NativeBinary<*>>(
    NativeBinary::class,
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
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

    abstract val runTaskProvider: AbstractExecTask<out org.gradle.api.tasks.AbstractExecTask<*>>?

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.plugin.mpp.Executable) {
        super.applyTo(receiver)

        entryPoint?.let(receiver::entryPoint)

        (runTaskProvider as AbstractExecTask<org.gradle.api.tasks.AbstractExecTask<*>>?)
            ?.let { runTaskProvider ->
                receiver.runTaskProvider?.get()?.let {
                    runTaskProvider.applyTo(it)
                }
            }
    }
}

@Serializable
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
    override val runTaskProvider: AbstractExecTaskImpl? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val entryPoint: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : Executable()

internal abstract class TestExecutable : NativeBinary<org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable>()

@Serializable
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val name: String? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : StaticLibrary()

internal abstract class SharedLibrary : AbstractNativeLibrary<org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary>()

@Serializable
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
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
    override val linkTaskProvider: KotlinNativeLinkImpl? = null,
    override val outputDirectory: String? = null,
    override val outputDirectoryProperty: String? = null,
    override val isStatic: Boolean? = null,
    val namePrefix: String = "",
    val buildTypes: Set<NativeBuildType> = NativeBuildType.DEFAULT_BUILD_TYPES,
) : Framework()
