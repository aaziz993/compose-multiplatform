package gradle.plugins.kotlin.benchmark

import gradle.api.ProjectNamed
import gradle.api.trySet
import gradle.collection.SerializableOptionalAnyList
import gradle.collection.SerializableOptionalAnyMap
import kotlinx.benchmark.gradle.BenchmarkConfiguration
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BenchmarkConfiguration(
    override val name: String? = null,
    val advanced: SerializableOptionalAnyMap? = null,
    val setAdvanced: SerializableOptionalAnyMap? = null,
    val excludes: Set<String>? = null,
    val setExcludes: Set<String>? = null,
    val includes: Set<String>? = null,
    val setIncludes: Set<String>? = null,
    val iterationTime: Long? = null,
    val iterationTimeUnit: String? = null,
    val iterations: Int? = null,
    val mode: String? = null,
    val outputTimeUnit: String? = null,
    val params: Map<String, SerializableOptionalAnyList>? = null,
    val setParams: Map<String, SerializableOptionalAnyList>? = null,
    val reportFormat: String? = null,
    val warmups: Int? = null,
) : ProjectNamed<BenchmarkConfiguration> {

    context(Project)
    override fun applyTo(recipient: BenchmarkConfiguration) {
        advanced?.let(recipient.advanced::putAll)

        setAdvanced?.let { setAdvanced ->
            recipient.advanced = setAdvanced.toMutableMap()
        }

        excludes?.let(recipient.excludes::addAll)
        recipient::excludes trySet setExcludes?.toMutableList()
        includes?.let(recipient.includes::addAll)
        recipient::includes trySet setIncludes?.toMutableList()
        recipient::iterationTime trySet iterationTime
        recipient::iterationTimeUnit trySet iterationTimeUnit
        recipient::iterations trySet iterations
        recipient::mode trySet mode
        recipient::outputTimeUnit trySet outputTimeUnit
        params?.mapValues { (_, value) -> value.toMutableList() }?.let(recipient.params::putAll)
        recipient::params trySet setParams?.mapValues { (_, value) -> value.toMutableList() }?.toMutableMap()
        recipient::reportFormat trySet reportFormat
        recipient::warmups trySet warmups
    }
}
