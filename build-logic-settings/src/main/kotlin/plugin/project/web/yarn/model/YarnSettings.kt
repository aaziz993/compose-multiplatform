package plugin.project.web.yarn.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import plugin.project.web.yarn.YarnResolution

@Serializable
internal data class YarnSettings(
    override val lockFileName: String? = null,
    override val lockFileDirectory: String? = null,
    override val ignoreScripts: Boolean? = null,
    override val yarnLockMismatchReport: YarnLockMismatchReport? = null,
    override val reportNewYarnLock: Boolean? = null,
    override val yarnLockAutoReplace: Boolean? = null,
    override val resolutions: List<YarnResolution>? = null,
) : YarnExtension
