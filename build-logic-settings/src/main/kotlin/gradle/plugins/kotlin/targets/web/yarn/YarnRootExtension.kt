package gradle.plugins.kotlin.targets.web.yarn

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.yarn
import gradle.api.tryAddAll
import gradle.api.trySet
import gradle.plugins.kotlin.targets.web.AbstractSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

@Serializable
internal data class YarnRootExtension(
    val lockFileName: String? = null,
    val lockFileDirectory: String? = null,
    val ignoreScripts: Boolean? = null,
    val yarnLockMismatchReport: YarnLockMismatchReport? = null,
    val reportNewYarnLock: Boolean? = null,
    val yarnLockAutoReplace: Boolean? = null,
    val resolutions: List<YarnResolution>? = null,
    val setResolutions: List<YarnResolution>? = null,
) : AbstractSettings<YarnRootExtension>() {

    context(Project)
    override fun applyTo(receiver: YarnRootExtension) {
        super.applyTo(receiver)

        receiver::lockFileName trySet lockFileName
        receiver::lockFileDirectory trySet lockFileDirectory?.let(project::file)
        receiver::ignoreScripts trySet ignoreScripts
        receiver::yarnLockMismatchReport trySet yarnLockMismatchReport
        receiver::reportNewYarnLock trySet reportNewYarnLock
        receiver::yarnLockAutoReplace trySet yarnLockAutoReplace
        receiver::resolutions tryAddAll resolutions?.map(YarnResolution::toYarnResolution)
        receiver::resolutions trySet setResolutions?.map(YarnResolution::toYarnResolution)?.toMutableList()
    }

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("gradle.node.plugin").id) {
            applyTo(project.yarn)
        }
}
