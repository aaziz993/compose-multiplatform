package plugin.project.web.npm.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport
import plugin.project.web.node.model.EnvSpec

@Serializable
internal data class NpmSettings(
    val command: String? = null,
    val lockFileName: String? = null,
    val lockFileDirectory: String? = null,
    val ignoreScripts: Boolean? = null,
    val packageLockMismatchReport: LockFileMismatchReport? = null,
    val reportNewPackageLock: Boolean? = null,
    val packageLockAutoReplace: Boolean? = null,
    val overrides: List<NpmOverride>? = null,
)
