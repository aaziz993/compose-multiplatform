package gradle.plugins.project

import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CIObjectTransformingContentPolymorphicSerializer::class)
internal sealed class CI {

    abstract val dependenciesCheck: Boolean

    abstract val signaturesCheck: Boolean

    abstract val formatCheck: Boolean

    abstract val qualityCheck: Boolean

    abstract val coverageVerify: Boolean

    abstract val docSamplesCheck: Boolean

    abstract val test: Boolean

    abstract val publishTo: LinkedHashSet<String>

    @Serializable
    data class GithubActions(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishTo: LinkedHashSet<String> = linkedSetOf(),
    ) : CI()

    @Serializable
    data class TeamCity(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishTo: LinkedHashSet<String> = linkedSetOf(),
    ) : CI()

    @Serializable
    data class JBSpaceAutomation(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishTo: LinkedHashSet<String> = linkedSetOf(),
        val runEnv: String? = null,
    ) : CI()
}

private object CIObjectTransformingContentPolymorphicSerializer
    : JsonObjectTransformingContentPolymorphicSerializer<CI>(CI::class)
