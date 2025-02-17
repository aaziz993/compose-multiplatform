package plugin.project.web.yarn.model

import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import plugin.project.web.yarn.YarnResolution

internal interface YarnExtension {
    val lockFileName: String?
    val lockFileDirectory: String?
    val ignoreScripts: Boolean?
    val yarnLockMismatchReport: YarnLockMismatchReport?
    val reportNewYarnLock: Boolean?
    val yarnLockAutoReplace: Boolean?
    val resolutions: List<YarnResolution>?
}
