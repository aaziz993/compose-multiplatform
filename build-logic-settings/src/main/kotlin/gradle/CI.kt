package gradle

import org.gradle.api.Project
import org.gradle.api.initialization.Settings

private val CI_DETECT_PROPERTIES = listOf(
    "CI",
    "GITHUB_ACTION",
    "JB_SPACE_EXECUTION_NUMBER",
    "TEAMCITY_VERSION",
)

internal val isCI: Boolean by lazy {
    CI_DETECT_PROPERTIES.any(System.getenv()::contains)
}

internal fun Settings.gitCommitId(): String = execute("git rev-parse --verify HEAD")

internal fun Settings.gitBranchName(): String = execute("git rev-parse --abbrev-ref HEAD")

internal fun Settings.gitStatus(): String = execute("git status --porcelain")

internal fun Project.gitCommitId(): String = execute("git rev-parse --verify HEAD")

internal fun Project.gitBranchName(): String = execute("git rev-parse --abbrev-ref HEAD")

internal fun Project.gitStatus(): String = execute("git status --porcelain")

// The GITHUB_REF_NAME provide the reference name.
public val gitRef: String? by lazy {
    System.getenv("GITHUB_REF_NAME")
}

// The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
// This number begins at 1 for the workflow's first run, and increments with each new run.
// This number does not change if you re-run the workflow run.
public val gitRunNumber: String? by lazy {
    System.getenv("GITHUB_RUN_NUMBER")
}

// The JB_SPACE_GIT_BRANCH provide the reference  as "refs/heads/repository_name".
public val spaceGitBranch: String? by lazy {
    System.getenv("JB_SPACE_GIT_BRANCH")
}

public val spaceExecutionNumber: String? by lazy {
    System.getenv("JB_SPACE_EXECUTION_NUMBER")
}

public val Project.teamCityBuildId: String?
    get() = property("teamcity.build.id")?.toString()

public val Project.teamCityBuildTypeId: String?
    get() = property("teamcity.buildType.id")?.toString()

public val Project.teamCityGitBranch: String?
    get() = execute(" echo %teamcity.build.branch%")

public val teamCityBuildNumber: String? by lazy {
    System.getenv("BUILD_NUMBER")
}
