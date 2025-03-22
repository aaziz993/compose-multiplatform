package gradle.plugins.kmp.jvm


import gradle.api.tasks.test.DefaultTestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.java.test.JUnitOptions
import gradle.plugins.java.test.JUnitPlatformOptions
import gradle.plugins.java.test.JavaDebugOptions
import gradle.plugins.java.JavaToolchainSpec
import gradle.plugins.java.ModularitySpec
import gradle.plugins.java.test.Test
import gradle.plugins.java.test.TestNGOptions
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

@Serializable
internal data class KotlinJvmTest(
    override val dryRun: Boolean? = null,
    override val modularity: ModularitySpec? = null,
    override val testClassesDirs: List<String>? = null,
    override val useJUnit: Boolean? = null,
    override val useJUnitDsl: JUnitOptions? = null,
    override val useJUnitPlatform: Boolean? = null,
    override val useJUnitPlatformDsl: JUnitPlatformOptions? = null,
    override val useTestNG: Boolean? = null,
    override val useTestNGDsl: TestNGOptions? = null,
    override val scanForTestClasses: Boolean? = null,
    override val forkEvery: Long? = null,
    override val maxParallelForks: Int? = null,
    override val binaryResultsDirectory: String? = null,
    override val ignoreFailures: Boolean? = null,
    override val testLogging: TestLoggingContainer? = null,
    override val testNameIncludePatterns: List<String>? = null,
    override val failFast: Boolean? = null,
    override val filter: DefaultTestFilter? = null,
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
    override val name: String? = null,,
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
) : Test() {

        context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(named)

        named as KotlinJvmTest

        named::targetName trySet targetName
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinJvmTest>())
}
