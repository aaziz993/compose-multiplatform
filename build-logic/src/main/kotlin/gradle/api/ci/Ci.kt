package gradle.api.ci

import gradle.api.ci.Ci.Github
import gradle.api.ci.Ci.TeamCity
import gradle.api.project.execute
import org.gradle.api.Project

private val GITHUB = Github()

private val TEAM_CITY = TeamCity()

public val CI: Ci?
    get() = when {
        System.getenv().contains(Github.KEY) -> GITHUB
        System.getenv().contains(TeamCity.KEY) -> TEAM_CITY
        else -> null
    }

public fun github(block: Github.() -> Unit): Unit = GITHUB.run(block)

public fun teamCity(block: TeamCity.() -> Unit): Unit = TEAM_CITY.run(block)

public sealed class Ci {

    public val name: String = this::class.simpleName!!

    context(_: Project)
    public abstract val branch: String

    context(_: Project)
    public abstract val run: String?

    public abstract var dependenciesCheck: Boolean

    public abstract var licenseeCheck: Boolean

    public abstract var formatCheck: Boolean

    public abstract var qualityCheck: Boolean

    public abstract var coverageReport: Boolean

    public abstract var coverageVerify: Boolean

    public abstract var docSamplesCheck: Boolean

    public abstract var test: Boolean

    private val versioning: Versioning = Versioning()

    public fun versioning(block: Versioning.() -> Unit): Unit = versioning.run(block)

    context(project: Project)
    public val buildMetadata: String
        get() = "${branch.takeIf { versioning.branch }.orEmpty()}${run?.takeIf { versioning.run }.orEmpty()}"

    public class Github internal constructor(
        override var dependenciesCheck: Boolean = true,
        override var licenseeCheck: Boolean = true,
        override var formatCheck: Boolean = true,
        override var qualityCheck: Boolean = true,
        override var coverageReport: Boolean = true,
        override var coverageVerify: Boolean = true,
        override var docSamplesCheck: Boolean = true,
        override var test: Boolean = true,
    ) : Ci() {

        context(_: Project)
        override val branch: String
            get() = ref

        context(_: Project)
        override val run: String?
            get() = runNumber

        public companion object {

            public const val KEY: String = "GITHUB_ACTION"

            // The GITHUB_REF_NAME provide the reference name.
            public val ref: String
                get() = System.getenv("GITHUB_REF_NAME") ?: "unknown"

            // The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
            // This number begins at 1 for the workflow's first run, and increments with each new run.
            // This number does not change if you re-run the workflow run.
            public val runNumber: String?
                get() = System.getenv("GITHUB_RUN_NUMBER")
        }
    }

    public class TeamCity internal constructor(
        override var dependenciesCheck: Boolean = true,
        override var licenseeCheck: Boolean = true,
        override var formatCheck: Boolean = true,
        override var qualityCheck: Boolean = true,
        override var coverageReport: Boolean = true,
        override var coverageVerify: Boolean = true,
        override var docSamplesCheck: Boolean = true,
        override var test: Boolean = true,
    ) : Ci() {

        context(project: Project)
        override val branch: String
            get() = gitBranch

        context(_: Project)
        override val run: String?
            get() = buildNumber

        public companion object {

            public const val KEY: String = "TEAMCITY_VERSION"

            context(project: Project)
            public val gitBranch: String
                get() = project.execute(" echo %teamcity.build.branch%")

            public val buildNumber: String?
                get() = System.getenv("BUILD_NUMBER")

            context(project: Project)
            public val buildId: String?
                get() = project.property("teamcity.build.id")?.toString()

            context(project: Project)
            public val buildTypeId: String?
                get() = project.property("teamcity.buildType.id")?.toString()
        }
    }
}
