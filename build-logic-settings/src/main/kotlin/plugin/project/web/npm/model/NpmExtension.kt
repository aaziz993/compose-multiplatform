package plugin.project.web.npm.model

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension

@Serializable
internal data class NpmExtension(
    val command: String? = null,
    val lockFileName: String? = null,
    val lockFileDirectory: String? = null,
    val ignoreScripts: Boolean? = null,
    val packageLockMismatchReport: LockFileMismatchReport? = null,
    val reportNewPackageLock: Boolean? = null,
    val packageLockAutoReplace: Boolean? = null,
    val overrides: List<NpmOverride>? = null,
) {

    context(Project)
    fun applyTo(extension: NpmExtension) {
        extension.command tryAssign command
        extension.lockFileName tryAssign lockFileName
        extension.lockFileDirectory tryAssign lockFileDirectory?.let(layout.projectDirectory::dir)
        extension.ignoreScripts tryAssign ignoreScripts
        extension.packageLockMismatchReport tryAssign packageLockMismatchReport
        extension.reportNewPackageLock tryAssign reportNewPackageLock
        extension.packageLockAutoReplace tryAssign packageLockAutoReplace
        extension.overrides tryAssign overrides?.map(NpmOverride::toNpmOverride)
    }
}
