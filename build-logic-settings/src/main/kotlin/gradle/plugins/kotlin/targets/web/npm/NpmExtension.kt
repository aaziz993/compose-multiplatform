package gradle.plugins.kotlin.targets.web.npm


import gradle.accessors.catalog.libs
import gradle.accessors.npm


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
        project.pluginManager.withPlugin(project.settings.libs.plugin("gradle.node.plugin").id) {
            project.npm.command tryAssign command
            project.npm.lockFileName tryAssign lockFileName
            project.npm.lockFileDirectory tryAssign lockFileDirectory?.let(project.layout.projectDirectory::dir)
            project.npm.ignoreScripts tryAssign ignoreScripts
            project.npm.packageLockMismatchReport tryAssign packageLockMismatchReport
            project.npm.reportNewPackageLock tryAssign reportNewPackageLock
            project.npm.packageLockAutoReplace tryAssign packageLockAutoReplace
            project.npm.overrides tryAssign overrides?.map(NpmOverride::toNpmOverride)
        }
}
