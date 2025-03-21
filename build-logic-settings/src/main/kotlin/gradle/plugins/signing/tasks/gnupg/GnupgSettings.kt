package gradle.plugins.signing.tasks.gnupg

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.security.internal.gnupg.GnupgSettings

@Serializable
internal data class GnupgSettings(
    val executable: String? = null,
    val homeDir: String? = null,
    val optionsFile: String? = null,
    val keyName: String? = null,
    val passphrase: String? = null,
    val useLegacyGpg: Boolean? = null,
) {

    context(Project)
    fun toGnupgSettings() = GnupgSettings().apply {
        this@GnupgSettings.executable?.let(::setExecutable)
        this@GnupgSettings.homeDir?.let(::file)?.let(::setHomeDir)
        this@GnupgSettings.optionsFile?.let(::file)?.let(::setOptionsFile)
        this@GnupgSettings.keyName?.let(::setKeyName)
        this@GnupgSettings.passphrase?.let(::setPassphrase)
        this@GnupgSettings.useLegacyGpg?.let(::setUseLegacyGpg)
    }
}
