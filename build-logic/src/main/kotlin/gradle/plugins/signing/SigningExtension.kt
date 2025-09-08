package gradle.plugins.signing

import gradle.api.initialization.settingsProperties
import gradle.api.project.settings
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningExtension

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun SigningExtension.generateGPGTasks(
    keyType: String = "RSA",
    keyLength: Int = 4096,
    subkeyType: String = " RSA",
    subkeyLength: Int = 4096,
    nameReal: String = project.settings.settingsProperties.developer.name!!,
    nameComment: String = "",
    nameEmail: String = project.settings.settingsProperties.developer.email!!,
    expireDate: Long = 0,
    passphrase: String,
): Unit = project.pluginManager.withPlugin("signing") {
    /** Distribute signing gpg key
     * There are 3 servers supported by Central servers: [ keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu ]
     */
    project.tasks.register<Exec>("gpgGenKey") {
        description = "Generates the signing GPG key"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-gen-key.sh").absolutePath

        args(
            keyType,
            keyLength,
            subkeyType,
            subkeyLength,
            nameReal,
            nameComment,
            nameEmail,
            expireDate,
            passphrase,
        )
    }

    project.tasks.register<Exec>("gpgKeyList") {
        description = "List the signing GPG keys"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-key-list.sh").absolutePath

        args(
            nameReal,
            passphrase,
        )
    }

    project.tasks.register<Exec>("gpgCleanKeys") {
        description = "Clean the signing GPG keys"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-clean-keys.sh").absolutePath

        args(
            nameReal,
        )
    }
}

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun SigningExtension.exportGPGTask(key: String): Unit = project.pluginManager.withPlugin("signing") {
    project.tasks.register<Exec>("gpgExportKey") {
        description = "Export the signing GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-export-key.sh").absolutePath

        args(key)
    }
}


