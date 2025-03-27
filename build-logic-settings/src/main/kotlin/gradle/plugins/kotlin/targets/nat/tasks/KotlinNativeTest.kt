package gradle.plugins.kotlin.targets.nat.tasks

import gradle.api.tasks.applyTo
import gradle.api.tasks.test.TestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.tryAssign
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.plugins.kotlin.targets.nat.TrackEnvironment
import gradle.plugins.kotlin.targets.nat.TrackEnvironmentKeyTransformingSerializer
import gradle.plugins.kotlin.tasks.KotlinTest
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

@Serializable
internal data class KotlinNativeTest(
    override val ignoreRunFailures: Boolean? = null,
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
    override val targetName: String? = null,
    val executables: List<String>? = null,
    val args: List<String>? = null,
    val workingDir: String? = null,
    val environment: SerializableAnyMap? = null,
    val trackEnvironments: List<@Serializable(with = TrackEnvironmentKeyTransformingSerializer::class) TrackEnvironment>? = null,
) : KotlinTest<KotlinNativeTest>() {

    context(Project)
    override fun applyTo(receiver: KotlinNativeTest) {
        super.applyTo(receiver)

        receiver.executableProperty tryAssign executables?.toTypedArray()?.let(project::files)
        receiver::args trySet args
        receiver::workingDir trySet workingDir

        environment?.let { environment ->
            receiver.environment = environment
        }

        trackEnvironments?.forEach { (name, tracked) ->
            receiver.trackEnvironment(name, tracked)
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<KotlinNativeTest>())
}
