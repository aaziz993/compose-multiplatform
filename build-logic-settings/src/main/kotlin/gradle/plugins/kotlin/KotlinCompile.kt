package gradle.plugins.kotlin


import gradle.api.tasks.K2MultiplatformCompilationTask
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.jvm.KotlinJvmCompile
import gradle.plugins.kmp.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kotlin.tasks.AbstractKotlinCompileTool
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Serializable
internal data class KotlinCompile(
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val jvmTargetValidationMode: JvmTargetValidationMode? = null,
    override val friendPaths: List<String>? = null,
    override val pluginClasspath: List<String>? = null,
    override val pluginOptions: List<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val kotlinJavaToolchain: KotlinJavaToolchain? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    /** A package prefix that is used for locating Java sources in a directory structure with non-full-depth packages.
     *
     * Example: a Java source file with `package com.example.my.package` is located in directory `src/main/java/my/package`.
     * Then, for the Kotlin compilation to locate the source file, use package prefix `"com.example"` */
    val javaPackagePrefix: String? = null,
    val usePreciseJavaTracking: Boolean? = null,
    val useKotlinAbiSnapshot: Boolean? = null,
    val classpathSnapshotProperties: ClasspathSnapshotProperties? = null,
) : AbstractKotlinCompileTool(), K2MultiplatformCompilationTask,
    KotlinJvmCompile {

        context(Project)
    override fun applyTo(named: T) {
        super<AbstractKotlinCompileTool>.applyTo(named)
        super<K2MultiplatformCompilationTask>.applyTo(named)
        super<KotlinJvmCompile>.applyTo(named)

        named as KotlinCompile

        named::javaPackagePrefix trySet javaPackagePrefix
        named::usePreciseJavaTracking trySet usePreciseJavaTracking
        named.useKotlinAbiSnapshot tryAssign useKotlinAbiSnapshot
        classpathSnapshotProperties?.applyTo(named.classpathSnapshotProperties)
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinCompile>())

    /** Properties related to the `kotlin.incremental.useClasspathSnapshot` feature. */
    @Serializable
    data class ClasspathSnapshotProperties(
        val useClasspathSnapshot: Boolean? = null,
        // Set if useClasspathSnapshot == true
        val classpathSnapshot: List<String>? = null,

        // Set if useClasspathSnapshot == false (to restore the existing classpath annotations when the feature is disabled)
        val classpath: List<String>? = null,

        // Set if useClasspathSnapshot == true
        val classpathSnapshotDir: String? = null,
    ) {

        context(Project)
        fun applyTo(properties: KotlinCompile.ClasspathSnapshotProperties) {
            properties.useClasspathSnapshot tryAssign useClasspathSnapshot
            classpathSnapshot?.let(properties.classpathSnapshot::setFrom)
            classpath?.let(properties.classpath::setFrom)
            properties.classpathSnapshotDir tryAssign classpathSnapshotDir?.let(layout.projectDirectory::dir)
        }
    }
}
