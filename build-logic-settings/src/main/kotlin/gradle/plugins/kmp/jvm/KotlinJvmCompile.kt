package gradle.plugins.kmp.jvm


import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.plugins.kmp.nat.CompilerPluginOptions
import gradle.plugins.kotlin.BaseKotlinCompile
import gradle.plugins.kotlin.KotlinJavaToolchain
import gradle.plugins.kotlin.UsesKotlinJavaToolchain
import gradle.plugins.kotlin.tasks.KotlinCompilationTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode

/**
 * Represents a Kotlin task compiling given Kotlin sources into JVM class files.
 */
internal interface KotlinJvmCompile : BaseKotlinCompile,
    KotlinCompilationTask<KotlinJvmCompilerOptions>,
    UsesKotlinJavaToolchain {

    /**
     * Controls JVM target validation mode between this task and the Java compilation task from Gradle for the same source set.
     *
     * Using the same JVM targets ensures that the produced JAR file contains class files of the same JVM bytecode version,
     * which is important to avoid compatibility issues for users of your code.
     *
     * The Gradle Java compilation task [org.gradle.api.tasks.compile.JavaCompile.targetCompatibility] controls the value
     * of the `org.gradle.jvm.version` [attribute](https://docs.gradle.org/current/javadoc/org/gradle/api/attributes/java/TargetJvmVersion.html)
     * which itself controls the produced artifact's minimum supported JVM version via
     * [Gradle Module Metadata](https://docs.gradle.org/current/userguide/publishing_gradle_module_metadata.html).
     * This allows Gradle to check the compatibility of dependencies at dependency resolution time.
     *
     * To avoid problems with different targets, we advise using the [JVM Toolchain](https://kotl.in/gradle/jvm/toolchain) feature.
     *
     * The default value for builds with Gradle <8.0 is [JvmTargetValidationMode.WARNING],
     * while for builds with Gradle 8.0+ it is [JvmTargetValidationMode.ERROR].
     *
     * @since 1.9.0
     */
    val jvmTargetValidationMode: JvmTargetValidationMode?

        context(Project)
    override fun applyTo(named: T) {
        super<BaseKotlinCompile>.applyTo(named)
        super<KotlinCompilationTask>.applyTo(named)
        super<UsesKotlinJavaToolchain>.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

        named.jvmTargetValidationMode tryAssign jvmTargetValidationMode
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>())
}

@Serializable
@SerialName("KotlinJvmCompile")
internal data class KotlinJvmCompileImpl(
    override val jvmTargetValidationMode: JvmTargetValidationMode? = null,
    override val friendPaths: List<String>? = null,
    override val pluginClasspath: List<String>? = null,
    override val pluginOptions: List<CompilerPluginOptions>? = null,
    override val moduleName: String? = null,
    override val sourceSetName: String? = null,
    override val multiPlatformEnabled: Boolean? = null,
    override val useModuleDetection: Boolean? = null,
    override val sources: List<String>? = null,
    override val setSources: List<String>? = null,
    override val libraries: List<String>? = null,
    override val destinationDirectory: String? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val dependsOn: SortedSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: SortedSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String = "",
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
    override val kotlinJavaToolchain: KotlinJavaToolchain?
) : KotlinJvmCompile
