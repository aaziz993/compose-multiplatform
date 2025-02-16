package plugin.project.web.yarn

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport

@Serializable
internal data class YarnSettings(
    val lockFileName: String? = null,
    val lockFileDirectory: String? = null,
    val ignoreScripts: Boolean? = null,
    val yarnLockMismatchReport: YarnLockMismatchReport? = null,
    val reportNewYarnLock: Boolean? = null,
    val yarnLockAutoReplace: Boolean? = null,
    val resolutions: List<YarnResolution>? = null,
)
