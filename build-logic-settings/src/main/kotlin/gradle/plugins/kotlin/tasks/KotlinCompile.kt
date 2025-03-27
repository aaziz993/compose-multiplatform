package gradle.plugins.kotlin.tasks

import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.targets.jvm.KotlinJvmCompile
import gradle.plugins.kotlin.targets.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.targets.nat.CompilerPluginOptions
import gradle.plugins.kotlin.tasks.KotlinJavaToolchain
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode

internal abstract class KotlinCompile<T : org.jetbrains.kotlin.gradle.tasks.KotlinCompile>
    : AbstractKotlinCompileTool<T>(), K2MultiplatformCompilationTask<T, org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions>,
    KotlinJvmCompile<T> {

    /** A package prefix that is used for locating Java sources in a directory structure with non-full-depth packages.
     *
     * Example: a Java source file with `package com.example.my.package` is located in directory `src/main/java/my/package`.
     * Then, for the Kotlin compilation to locate the source file, use package prefix `"com.example"` */
    abstract val javaPackagePrefix: String?
    abstract val usePreciseJavaTracking: Boolean?
    abstract val useKotlinAbiSnapshot: Boolean?
    abstract val classpathSnapshotProperties: ClasspathSnapshotProperties?

    context(Project)
    override fun applyTo(receiver: T) {
        super<AbstractKotlinCompileTool>.applyTo(receiver)
        super<K2MultiplatformCompilationTask>.applyTo(receiver)
        super<KotlinJvmCompile>.applyTo(receiver)

        receiver::javaPackagePrefix trySet javaPackagePrefix
        receiver::usePreciseJavaTracking trySet usePreciseJavaTracking
        receiver.useKotlinAbiSnapshot tryAssign useKotlinAbiSnapshot
        classpathSnapshotProperties?.applyTo(receiver.classpathSnapshotProperties)
    }

    /** Properties related to the `kotlin.incremental.useClasspathSnapshot` feature. */
    @Serializable
    data class ClasspathSnapshotProperties(
        val useClasspathSnapshot: Boolean? = null,
        // Set if useClasspathSnapshot == true
        val classpathSnapshot: Set<String>? = null,
        val setClasspathSnapshot: Set<String>? = null,

        // Set if useClasspathSnapshot == false (to restore the existing classpath annotations when the feature is disabled)
        val classpath: Set<String>? = null,
        val setClasspath: Set<String>? = null,

        // Set if useClasspathSnapshot == true
        val classpathSnapshotDir: String? = null,
    ) {

        context(Project)
        fun applyTo(receiver: org.jetbrains.kotlin.gradle.tasks.KotlinCompile.ClasspathSnapshotProperties) {
            receiver.useClasspathSnapshot tryAssign useClasspathSnapshot
            classpathSnapshot?.toTypedArray()?.let(receiver.classpathSnapshot::from)
            setClasspathSnapshot?.let(receiver.classpathSnapshot::setFrom)
            classpath?.toTypedArray()?.let(receiver.classpath::from)
            setClasspath?.let(receiver.classpath::setFrom)
            receiver.classpathSnapshotDir tryAssign classpathSnapshotDir?.let(project.layout.projectDirectory::dir)
        }
    }
}

@Serializable
internal data class KotlinCompileImpl(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val jvmTargetValidationMode: JvmTargetValidationMode? = null,
    override val friendPaths: Set<String>? = null,
    override val setFriendPaths: Set<String>? = null,
    override val pluginClasspath: Set<String>? = null,
    override val setPluginClasspath: Set<String>? = null,
    override val pluginOptions: Set<CompilerPluginOptions>? = null,
    override val setPluginOptions: Set<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val kotlinJavaToolchain: KotlinJavaToolchain? = null,
    override val sources: Set<String>? = null,
    override val setSources: Set<String>? = null,
    override val libraries: Set<String>? = null,
    override val setLibraries: Set<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val javaPackagePrefix: String? = null,
    override val usePreciseJavaTracking: Boolean? = null,
    override val useKotlinAbiSnapshot: Boolean? = null,
    override val classpathSnapshotProperties: ClasspathSnapshotProperties? = null,
) : KotlinCompile<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>())
}
