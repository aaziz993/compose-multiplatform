package plugin.project.web.yarn.model

import gradle.trySet
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
) {

    context(Project)
    fun applyTo(extension: YarnRootExtension) {
        extension::lockFileName trySet lockFileName
        extension::lockFileDirectory trySet lockFileDirectory?.let(::file)
        extension::ignoreScripts trySet ignoreScripts
        extension::yarnLockMismatchReport trySet yarnLockMismatchReport
        extension::reportNewYarnLock trySet reportNewYarnLock
        extension::yarnLockAutoReplace trySet yarnLockAutoReplace
        resolutions?.map(YarnResolution::toYarnResolution)?.let(extension.resolutions::addAll)
        extension::lockFileDirectory trySet lockFileDirectory?.let(::file)

        extension.yarnLockMismatchReport = YarnLockMismatchReport.valueOf(
            providers.gradleProperty("npm.packageLockMismatchReport").get().uppercase(),
        )

        extension.ignoreScripts = providers.gradleProperty("ignoreScripts").get().toBoolean()
    }
}
