package gradle

import org.gradle.api.Project
import org.gradle.api.initialization.Settings

public val isCI: Boolean by lazy {
    System.getenv("CI_VERSION") != null
}

public fun Settings.gitCommitId(): String = execute("git rev-parse --verify HEAD")

public fun Settings.gitBranchName(): String = execute("git rev-parse --abbrev-ref HEAD")

public fun Settings.gitStatus(): String = execute("git status --porcelain")

public fun Project.gitCommitId(): String = execute("git rev-parse --verify HEAD")

public fun Project.gitBranchName(): String = execute("git rev-parse --abbrev-ref HEAD")

public fun Project.gitStatus(): String = execute("git status --porcelain")

// The GITHUB_REF_NAME provide the reference name.
public val gitRef: String? by lazy {
    System.getenv()["GITHUB_REF_NAME"]
}

// The GITHUB_RUN_NUMBER A unique number for each run of a particular workflow in a repository.
// This number begins at 1 for the workflow's first run, and increments with each new run.
// This number does not change if you re-run the workflow run.
public val gitRunNumber: String? by lazy {
    System.getenv()["GITHUB_RUN_NUMBER"]
}

// The JB_SPACE_GIT_BRANCH provide the reference  as "refs/heads/repository_name".
public val spaceGitBranch: String? by lazy {
    System.getenv()["JB_SPACE_GIT_BRANCH"]
}

public val spaceExecutionNumber: String? by lazy {
    System.getenv()["JB_SPACE_EXECUTION_NUMBER"]
}
