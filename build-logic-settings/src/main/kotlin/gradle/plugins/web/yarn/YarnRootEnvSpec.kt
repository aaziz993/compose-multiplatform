package gradle.plugins.web.yarn

import gradle.accessors.yarn
import gradle.accessors.yarnEnv
import gradle.api.tryAssign
import gradle.collection.act
import gradle.plugins.web.EnvSpec
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport

/**
 * Spec for Yarn - package manager to install NPM dependencies
 */
@Serializable
internal data class YarnRootEnvSpec(
    override val download: Boolean? = null,
    override val downloadBaseUrl: String? = null,
    override val installationDirectory: String? = null,
    override val version: String? = null,
    override val command: String? = null,
    /**
     * Specify whether to not run install without custom package scripts.
     * It is useful for security
     *
     * Default: true
     */
    val ignoreScripts: Boolean? = null,
    /**
     * Specify a behaviour if yarn.lock file was changed
     *
     * Default: FAIL
     */
    val yarnLockMismatchReport: YarnLockMismatchReport? = null,
    /**
     * Specify whether to fail a build if new yarn.lock file was generated during the build
     *
     * Default: false
     */
    val reportNewYarnLock: Boolean? = null,
    /**
     * Specify whether to replace already existing yarn.lock file with newly generated yarn.lock file
     *
     * Default: false
     */
    val yarnLockAutoReplace: Boolean? = null,
    /**
     * Specify replacements of versions of installed NPM dependencies
     *
     * Details: https://classic.yarnpkg.com/lang/en/docs/selective-version-resolutions/
     */
    val resolutions: List<YarnResolution>? = null,
    val setResolutions: List<YarnResolution>? = null,
) : EnvSpec() {

    context(Project)
    fun applyTo() {
        super.applyTo(yarnEnv)

        yarnEnv.ignoreScripts tryAssign ignoreScripts
        yarnEnv.yarnLockMismatchReport tryAssign yarnLockMismatchReport
        yarnEnv.reportNewYarnLock tryAssign reportNewYarnLock
        yarnEnv.yarnLockAutoReplace tryAssign yarnLockAutoReplace
        resolutions?.map(YarnResolution::toYarnResolution)?.let(yarnEnv.resolutions::addAll)
        setResolutions?.act(yarn.resolutions::clear)?.map(YarnResolution::toYarnResolution)?.let(yarnEnv.resolutions::addAll)
    }
}
