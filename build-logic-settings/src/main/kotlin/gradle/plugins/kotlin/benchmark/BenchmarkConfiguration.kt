package gradle.plugins.kotlin.benchmark

import gradle.api.BaseNamed
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.collection.SerializableOptionalAnyList
import kotlinx.benchmark.gradle.BenchmarkConfiguration
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class BenchmarkConfiguration(
    val advanced: SerializableAnyMap? = null,
    val excludes: List<String>? = null,
    val includes: List<String>? = null,
    val iterationTime: Long? = null,
    val iterationTimeUnit: String? = null,
    val iterations: Int? = null,
    val mode: String? = null,
    override val name: String = "",
    val outputTimeUnit: String? = null,
    val params: Map<String, SerializableOptionalAnyList>? = null,
    val reportFormat: String? = null,
    val warmups: Int? = null,
) : BaseNamed<BenchmarkConfiguration> {

    context(Project)
    override fun applyTo(named: BenchmarkConfiguration) {
        advanced?.forEach(named::advanced)
        excludes?.let(named.excludes::addAll)
        includes?.let(named.includes::addAll)
        named::iterationTime trySet iterationTime
        named::iterationTimeUnit trySet iterationTimeUnit
        named::iterations trySet iterations
        named::mode trySet mode
        named::outputTimeUnit trySet outputTimeUnit
        named::params trySet params?.mapValues { (_, value) -> value.toMutableList() }?.toMutableMap()
        named::reportFormat trySet reportFormat
        named::warmups trySet warmups
    }
}
