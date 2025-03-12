package gradle.plugins.kotlin

import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.tasks.test.AbstractTestTask
import gradle.tasks.test.DefaultTestFilter
import gradle.tasks.test.TestLoggingContainer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project

internal abstract class KotlinTest : AbstractTestTask() {

    abstract val targetName: String?
    abstract val ignoreRunFailures: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.KotlinTest

        named::targetName trySet targetName
        named::ignoreRunFailures trySet ignoreRunFailures
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
    override val filter: DefaultTestFilter? = null,
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
) : KotlinTest()
