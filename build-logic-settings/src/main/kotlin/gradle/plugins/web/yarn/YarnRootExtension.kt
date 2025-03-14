package gradle.plugins.web.yarn

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.yarn
import gradle.api.trySet
import gradle.plugins.web.AbstractSettings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport

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
) : AbstractSettings() {

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
            super.applyTo(yarn)

            yarn::lockFileName trySet lockFileName
            yarn::lockFileDirectory trySet lockFileDirectory?.let(::file)
            yarn::ignoreScripts trySet ignoreScripts
            yarn::yarnLockMismatchReport trySet yarnLockMismatchReport
            yarn::reportNewYarnLock trySet reportNewYarnLock
            yarn::yarnLockAutoReplace trySet yarnLockAutoReplace
            resolutions?.map(YarnResolution::toYarnResolution)?.let(yarn.resolutions::addAll)
            yarn::lockFileDirectory trySet lockFileDirectory?.let(::file)

            yarn.yarnLockMismatchReport = YarnLockMismatchReport.valueOf(
                providers.gradleProperty("npm.packageLockMismatchReport").get().uppercase(),
            )

            yarn.ignoreScripts = providers.gradleProperty("ignoreScripts").get().toBoolean()
        }
}
