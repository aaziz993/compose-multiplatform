package gradle.plugins.project

import gradle.serialization.serializer.JsonKeyValueTransformingContentPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CIKeyValueTransformingContentPolymorphicSerializer::class)
internal sealed class CI {

    abstract val dependenciesCheck: Boolean

    abstract val signaturesCheck: Boolean

    abstract val formatCheck: Boolean

    abstract val qualityCheck: Boolean

    abstract val coverageVerify: Boolean

    abstract val docSamplesCheck: Boolean

    abstract val test: Boolean

    abstract val publishToGithubPackages: Boolean

    abstract val publishToSpacePackages: Boolean

    abstract val publishToMavenRepository: Boolean

    @Serializable
    data class GithubActions(
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishToGithubPackages: Boolean = true,
        override val publishToSpacePackages: Boolean = false,
        override val publishToMavenRepository: Boolean = false,
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
        override val publishToGithubPackages: Boolean = false,
        override val publishToSpacePackages: Boolean = true,
        override val publishToMavenRepository: Boolean = true,
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
        override val publishToGithubPackages: Boolean = false,
        override val publishToSpacePackages: Boolean = true,
        override val publishToMavenRepository: Boolean = true,
        val runEnv: String? = null,
    ) : CI()
}

private object CIKeyValueTransformingContentPolymorphicSerializer
    : JsonKeyValueTransformingContentPolymorphicSerializer<CI>(CI::class)
