package gradle.plugins.signing.tasks.gnupg

import gradle.api.trySet
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
        ::setExecutable trySet this@GnupgSettings.executable
        ::setHomeDir trySet this@GnupgSettings.homeDir?.let(project::file)
        ::setOptionsFile trySet this@GnupgSettings.optionsFile?.let(project::file)
        ::setKeyName trySet this@GnupgSettings.keyName
        ::setPassphrase trySet this@GnupgSettings.passphrase
        ::setUseLegacyGpg trySet this@GnupgSettings.useLegacyGpg
    }
}
