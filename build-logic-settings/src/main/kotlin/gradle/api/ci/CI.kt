package gradle.api.ci

import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable(with = CIObjectTransformingContentPolymorphicSerializer::class)
internal sealed class CI {

    abstract val name: String

    abstract val dependenciesCheck: Boolean

    abstract val signaturesCheck: Boolean

    abstract val formatCheck: Boolean

    abstract val qualityCheck: Boolean

    abstract val coverageVerify: Boolean

    abstract val docSamplesCheck: Boolean

    abstract val test: Boolean

    abstract val publishRepositories: LinkedHashSet<PublishRepository>

    @Serializable
    data class GithubActions(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<PublishRepository> = linkedSetOf(),
    ) : CI() {

        @Transient
        override val name: String = "github"
    }

    @Serializable
    data class TeamCity(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<PublishRepository> = linkedSetOf(),
    ) : CI() {

        @Transient
        override val name: String = "teamcity"
    }

    @Serializable
    data class JBSpaceAutomation(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<PublishRepository> = linkedSetOf(),
        val runEnv: String? = null,
    ) : CI() {

        @Transient
        override val name: String = "space"
    }
}

private object CIObjectTransformingContentPolymorphicSerializer
    : JsonObjectTransformingContentPolymorphicSerializer<CI>(CI::class)
