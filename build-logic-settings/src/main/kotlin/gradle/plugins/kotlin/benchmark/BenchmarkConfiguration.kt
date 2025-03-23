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
    override fun applyTo(receiver: BenchmarkConfiguration) {
        advanced?.let(receiver.advanced::putAll)

        setAdvanced?.let { setAdvanced ->
            receiver.advanced = setAdvanced.toMutableMap()
        }

        excludes?.let(receiver.excludes::addAll)
        receiver::excludes trySet setExcludes?.toMutableList()
        includes?.let(receiver.includes::addAll)
        receiver::includes trySet setIncludes?.toMutableList()
        receiver::iterationTime trySet iterationTime
        receiver::iterationTimeUnit trySet iterationTimeUnit
        receiver::iterations trySet iterations
        receiver::mode trySet mode
        receiver::outputTimeUnit trySet outputTimeUnit
        params?.mapValues { (_, value) -> value.toMutableList() }?.let(receiver.params::putAll)
        receiver::params trySet setParams?.mapValues { (_, value) -> value.toMutableList() }?.toMutableMap()
        receiver::reportFormat trySet reportFormat
        receiver::warmups trySet warmups
    }
}
