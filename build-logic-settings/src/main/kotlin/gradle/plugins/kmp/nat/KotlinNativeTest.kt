package gradle.plugins.kmp.nat


import gradle.api.tasks.test.DefaultTestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.tasks.KotlinTest
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

@Serializable
internal data class KotlinNativeTest(
    override val targetName: String? = null,
    override val ignoreRunFailures: Boolean? = null,
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
    override val name: String = "",
    val executables: List<String>? = null,
    val args: List<String>? = null,
    val workingDir: String? = null,
    val environment: SerializableAnyMap? = null,
    val trackEnvironments: List<@Serializable(with = TrackEnvironmentTransformingSerializer::class) TrackEnvironment>? = null,
) : KotlinTest() {

        context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(named)

        named as KotlinNativeTest

        named.executableProperty tryAssign executables?.toTypedArray()?.let(::files)
        named::args trySet args
        named::workingDir trySet workingDir

        environment?.let { environment ->
            named.environment = environment
        }

        trackEnvironments?.forEach { (name, tracked) ->
            named.trackEnvironment(name, tracked)
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KotlinNativeTest>())
}
