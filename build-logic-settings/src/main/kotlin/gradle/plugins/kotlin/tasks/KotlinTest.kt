package gradle.plugins.kotlin.tasks

import gradle.api.tasks.applyTo
import gradle.api.tasks.test.AbstractTestTask
import gradle.api.tasks.test.TestFilter
import gradle.api.tasks.test.TestLoggingContainer
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal abstract class KotlinTest<T : org.jetbrains.kotlin.gradle.tasks.KotlinTest> : AbstractTestTask<T>() {

    abstract val targetName: String?
    abstract val ignoreRunFailures: Boolean?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::targetName trySet targetName
        receiver::ignoreRunFailures trySet ignoreRunFailures
    }
}

@Serializable
@SerialName("KotlinTest")
internal data class KotlinTestImpl(
    override val targetName: String? = null,
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
) : KotlinTest<org.jetbrains.kotlin.gradle.tasks.KotlinTest>() {

    context(project: Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinTest>())
}
