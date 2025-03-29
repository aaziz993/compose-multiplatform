package gradle.plugins.kotlin.benchmark

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.tryAddAll
import gradle.api.tryPutAll
import gradle.api.trySet
import gradle.collection.SerializableOptionalAnyList
import gradle.collection.SerializableOptionalAnyMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = BenchmarkConfigurationObjectTransformingSerializer::class)
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
) : ProjectNamed<kotlinx.benchmark.gradle.BenchmarkConfiguration> {

    context(Project)
    override fun applyTo(receiver: kotlinx.benchmark.gradle.BenchmarkConfiguration) {
        receiver.advanced tryPutAll advanced
        receiver.advanced trySet setAdvanced?.toMutableMap()
        receiver.excludes tryAddAll excludes
        receiver::excludes trySet setExcludes?.toMutableList()
        receiver.includes tryAddAll includes
        receiver::includes trySet setIncludes?.toMutableList()
        receiver::iterationTime trySet iterationTime
        receiver::iterationTimeUnit trySet iterationTimeUnit
        receiver::iterations trySet iterations
        receiver::mode trySet mode
        receiver::outputTimeUnit trySet outputTimeUnit
        receiver.params tryPutAll params?.mapValues { (_, value) -> value.toMutableList() }
        receiver::params trySet setParams?.mapValues { (_, value) -> value.toMutableList() }?.toMutableMap()
        receiver::reportFormat trySet reportFormat
        receiver::warmups trySet warmups
    }
}

private object BenchmarkConfigurationObjectTransformingSerializer
    : NamedObjectTransformingSerializer<BenchmarkConfiguration>(BenchmarkConfiguration.generatedSerializer())
