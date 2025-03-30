package gradle.api.ci

import gradle.accessors.execute
import gradle.serialization.serializer.JsonObjectTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@Serializable(with = CIObjectTransformingContentPolymorphicSerializer::class)
internal sealed class CI {

    abstract val versioning: Versioning

    context(Project)
    abstract val branch: String?

    context(Project)
    abstract val runNumber: String?

    abstract val dependenciesCheck: Boolean

    abstract val signaturesCheck: Boolean

    abstract val formatCheck: Boolean

    abstract val qualityCheck: Boolean

    abstract val coverageVerify: Boolean

    abstract val docSamplesCheck: Boolean

    abstract val test: Boolean

    abstract val publishRepositories: LinkedHashSet<PublishRepository>

    @Serializable
    data class Github(
        override val versioning: Versioning = Versioning(),
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<PublishRepository> = linkedSetOf(),
    ) : CI() {

        init {
            versioning.ci = this
        }

        context(Project)
        override val branch: String?
            get() = ref

        context(Project)
        override val runNumber: String?
            get() = Github.runNumber

        companion object {

            const val CI_KEY = "GITHUB_ACTION"

            // The GITHUB_REF_NAME provide the reference name.
            val ref: String?
                get() = System.getenv("GITHUB_REF_NAME")

            // The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
            // This number begins at 1 for the workflow's first run, and increments with each new run.
            // This number does not change if you re-run the workflow run.
            val runNumber: String?
                get() = System.getenv("GITHUB_RUN_NUMBER")

            context(Settings)
            val commitId: String
                get() = execute("git rev-parse --verify HEAD")

            context(Settings)
            val status: String
                get() = execute("git status --porcelain")
        }
    }

    @Serializable
    data class TeamCity(
        override val versioning: Versioning = Versioning(),
        override val dependenciesCheck: Boolean = true,
        override val signaturesCheck: Boolean = true,
        override val formatCheck: Boolean = true,
        override val qualityCheck: Boolean = true,
        override val coverageVerify: Boolean = true,
        override val docSamplesCheck: Boolean = true,
        override val test: Boolean = true,
        override val publishRepositories: LinkedHashSet<PublishRepository> = linkedSetOf(),
    ) : CI() {

        init {
            versioning.ci = this
        }

        context(Project)
        override val branch: String?
            get() = gitBranch

        context(Project)
        override val runNumber: String?
            get() = buildNumber

        companion object {

            const val CI_KEY = "TEAMCITY_VERSION"

            context(Project)
            val gitBranch: String?
                get() = execute(" echo %teamcity.build.branch%")

            val buildNumber: String?
                get() = System.getenv("BUILD_NUMBER")

            context(Project)
            val buildId: String?
                get() = property("teamcity.build.id")?.toString()

            context(Project)
            val buildTypeId: String?
                get() = property("teamcity.buildType.id")?.toString()
        }
    }

    companion object {

        const val CI_KEY = "CI"

        private val CI_KEYS_NAMES = mapOf(
            Github.CI_KEY to Github::class.simpleName,
            TeamCity.CI_KEY to TeamCity::class.simpleName,
            CI_KEY to "CI",
        )

        val github: Boolean by lazy {
            System.getenv().contains(Github.CI_KEY)
        }

        val teamCity: Boolean by lazy {
            System.getenv().contains(TeamCity.CI_KEY)
        }

        val present: Boolean by lazy {
            CI_KEYS_NAMES.keys.any(System.getenv()::contains)
        }

        val name: String? by lazy {
            CI_KEYS_NAMES.entries.find { (key, _) -> System.getenv().contains(key) }?.value
        }
    }
}

private object CIObjectTransformingContentPolymorphicSerializer
    : JsonObjectTransformingContentPolymorphicSerializer<CI>(CI::class)

internal val Collection<CI>.current
    get() = find { ci -> ci::class.simpleName == CI.name }
