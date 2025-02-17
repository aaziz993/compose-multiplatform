package gradle

import org.gradle.api.initialization.Settings

internal fun Settings.execute(cmd: String): String {
    return settings.providers.exec {
        commandLine(cmd.split(" "))
    }.standardOutput.asText.get().trim()
}
