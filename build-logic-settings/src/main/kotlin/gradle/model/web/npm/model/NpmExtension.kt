package gradle.model.web.npm.model

import gradle.id
import gradle.libs
import gradle.npm
import gradle.plugin
import gradle.plugins
import gradle.settings
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport

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
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
            npm.command tryAssign command
            npm.lockFileName tryAssign lockFileName
            npm.lockFileDirectory tryAssign lockFileDirectory?.let(layout.projectDirectory::dir)
            npm.ignoreScripts tryAssign ignoreScripts
            npm.packageLockMismatchReport tryAssign packageLockMismatchReport
            npm.reportNewPackageLock tryAssign reportNewPackageLock
            npm.packageLockAutoReplace tryAssign packageLockAutoReplace
            npm.overrides tryAssign overrides?.map(NpmOverride::toNpmOverride)
        }
}
