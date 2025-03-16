package gradle.plugins.kotlin.benchmark

import gradle.api.trySet
import gradle.collection.SerializableAnyList
import gradle.collection.SerializableAnyMap
import gradle.collection.SerializableOptionalAnyList
import kotlinx.benchmark.gradle.BenchmarkConfiguration
import kotlinx.serialization.Serializable

@Serializable
internal data class BenchmarkConfiguration(
    val advanced: SerializableAnyMap? = null,
    val excludes: List<String>? = null,
    val includes: List<String>? = null,
    val iterationTime: Long? = null,
    val iterationTimeUnit: String? = null,
    val iterations: Int? = null,
    val mode: String? = null,
    val name: String = "",
    val outputTimeUnit: String? = null,
    val params: Map<String, SerializableOptionalAnyList>? = null,
    val reportFormat: String? = null,
    val warmups: Int? = null,
) {

    fun applyTo(configuration: BenchmarkConfiguration) {
        advanced?.forEach(configuration::advanced)
        excludes?.let(configuration.excludes::addAll)
        includes?.let(configuration.includes::addAll)
        configuration::iterationTime trySet iterationTime
        configuration::iterationTimeUnit trySet iterationTimeUnit
        configuration::iterations trySet iterations
        configuration::mode trySet mode
        configuration::outputTimeUnit trySet outputTimeUnit
        configuration::params trySet params?.mapValues { (_, value) -> value.toMutableList() }?.toMutableMap()
        configuration::reportFormat trySet reportFormat
        configuration::warmups trySet warmups
    }
}
