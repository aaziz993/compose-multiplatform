package plugin.project.web.npm.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport
import plugin.project.web.node.model.EnvSpec

@Serializable
internal data class NpmSettings(
    override val command: String? = null,
    override val lockFileName: String? = null,
    override val lockFileDirectory: String? = null,
    override val ignoreScripts: Boolean? = null,
    override val packageLockMismatchReport: LockFileMismatchReport? = null,
    override val reportNewPackageLock: Boolean? = null,
    override val packageLockAutoReplace: Boolean? = null,
    override val overrides: List<NpmOverride>? = null,
) : NpmExtension
