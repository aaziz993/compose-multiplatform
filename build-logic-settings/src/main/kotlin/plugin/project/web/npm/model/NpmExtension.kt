package plugin.project.web.npm.model

import org.jetbrains.kotlin.gradle.targets.js.npm.LockFileMismatchReport

internal interface NpmExtension {
    val command: String?
    val lockFileName: String?
    val lockFileDirectory: String?
    val ignoreScripts: Boolean?
    val packageLockMismatchReport: LockFileMismatchReport?
    val reportNewPackageLock: Boolean?
    val packageLockAutoReplace: Boolean?
    val overrides: List<NpmOverride>?
}
