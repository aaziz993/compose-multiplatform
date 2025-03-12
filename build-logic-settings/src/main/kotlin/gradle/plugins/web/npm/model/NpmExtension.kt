package gradle.plugins.web.npm.model

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.npm
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
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
