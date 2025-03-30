package gradle.plugins.project

import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable(with = CIObjectTransformingContentPolymorphicSerializer::class)
public sealed class CI {

    public abstract val name: String

    public abstract val dependenciesCheck: Boolean

    public abstract val signaturesCheck: Boolean

    public abstract val formatCheck: Boolean

    public abstract val qualityCheck: Boolean

    public abstract val coverageVerify: Boolean

    public abstract val docSamplesCheck: Boolean

    public abstract val test: Boolean

    public abstract val publishRepositories: Map<String, Boolean>

    @Serializable
    public data class GithubActions(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: Map<String, Boolean> = emptyMap(),
    ) : CI() {

        @Transient
        override val name: String = "github"
    }

    @Serializable
    public data class TeamCity(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<String> = linkedSetOf(),
    ) : CI() {

        @Transient
        override val name: String = "teamcity"
    }

    @Serializable
    public data class JBSpaceAutomation(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<String> = linkedSetOf(),
        public val runEnv: String? = null,
    ) : CI() {

        @Transient
        override val name: String = "space"
    }

    public companion object {

        public val name: String?
            get() = CI::class.sealedSubclasses.
    }
}

private object CIObjectTransformingContentPolymorphicSerializer
    : JsonObjectTransformingContentPolymorphicSerializer<CI>(CI::class)
