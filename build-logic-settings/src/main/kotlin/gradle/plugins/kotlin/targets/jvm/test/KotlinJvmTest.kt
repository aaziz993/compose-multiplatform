package gradle.plugins.kotlin.targets.jvm.test

import gradle.api.tasks.applyTo
import gradle.api.tasks.test.TestFilter
import gradle.api.tasks.test.TestLoggingContainer
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.java.ModularitySpec
import gradle.plugins.java.test.JUnitContentPolymorphicSerializer
import gradle.plugins.java.test.JUnitPlatformContentPolymorphicSerializer
import gradle.plugins.java.test.JavaDebugOptions
import gradle.plugins.java.test.Test
import gradle.plugins.java.test.TestNGContentPolymorphicSerializer
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

@Serializable
internal data class KotlinJvmTest(
    override val dryRun: Boolean? = null,
    override val modularity: ModularitySpec? = null,
    override val testClassesDirs: Set<String>? = null,
    override val useJUnit: @Serializable(with = JUnitContentPolymorphicSerializer::class) Any? = null,
    override val useJUnitPlatform: @Serializable(with = JUnitPlatformContentPolymorphicSerializer::class) Any? = null,
    override val useTestNG: @Serializable(with = TestNGContentPolymorphicSerializer::class) Any? = null,
    override val scanForTestClasses: Boolean? = null,
    override val forkEvery: Long? = null,
    override val maxParallelForks: Int? = null,
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: TestFilter? = null,
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
    override val systemProperties: SerializableAnyMap? = null,
    override val setEnvironment: SerializableAnyMap? = null,
    override val defaultCharacterEncoding: String? = null,
    override val minHeapSize: String? = null,
    override val maxHeapSize: String? = null,
    override val jvmArgs: List<String>? = null,
    override val setJvmArgs: List<String>? = null,
    override val bootstrapClasspath: List<String>? = null,
    override val setBootstrapClasspath: List<String>? = null,
    override val enableAssertions: Boolean? = null,
    override val debug: Boolean? = null,
    override val debugOptions: JavaDebugOptions? = null,
    override val allJvmArgs: List<String>? = null,
    override val setAllJvmArgs: List<String>? = null,
    override val executable: String? = null,
    override val workingDir: String? = null,
    override val environment: SerializableAnyMap? = null,
    override val setSystemProperties: SerializableAnyMap? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val javaLauncher: JavaToolchainSpec? = null,
    val targetName: String? = null,
) : Test<KotlinJvmTest>() {

    context(Project)
    override fun applyTo(receiver: KotlinJvmTest) {
        super.applyTo(receiver)

        receiver::targetName trySet targetName
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinJvmTest>())
}
