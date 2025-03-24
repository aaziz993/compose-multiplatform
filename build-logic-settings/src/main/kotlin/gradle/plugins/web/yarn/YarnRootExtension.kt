package gradle.plugins.web.yarn

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.yarn
import gradle.api.trySet
import gradle.collection.act
import gradle.plugins.web.AbstractSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnEnv
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec

@Serializable
internal data class YarnRootExtension(
    override val downloadBaseUrl: String? = null,
    override val version: String? = null,
    val lockFileName: String? = null,
    val lockFileDirectory: String? = null,
    val ignoreScripts: Boolean? = null,
    val yarnLockMismatchReport: YarnLockMismatchReport? = null,
    val reportNewYarnLock: Boolean? = null,
    val yarnLockAutoReplace: Boolean? = null,
    val resolutions: List<YarnResolution>? = null,
    val setResolutions: List<YarnResolution>? = null,
) : AbstractSettings<YarnEnv>() {

    context(project: Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("gradle.node.plugin").id) {
            super.applyTo(project.yarn)
            YarnRootEnvSpec
            project.yarn::lockFileName trySet lockFileName
            project.yarn::lockFileDirectory trySet lockFileDirectory?.let(project::file)
            project.yarn::ignoreScripts trySet ignoreScripts
            project. yarn::yarnLockMismatchReport trySet yarnLockMismatchReport
            project.yarn::reportNewYarnLock trySet reportNewYarnLock
            project.yarn::yarnLockAutoReplace trySet yarnLockAutoReplace
            resolutions?.map(YarnResolution::toYarnResolution)?.let(project.yarn.resolutions::addAll)
            setResolutions?.act(project.yarn.resolutions::clear)?.map(YarnResolution::toYarnResolution)?.let(project.yarn.resolutions::addAll)
        }
}
