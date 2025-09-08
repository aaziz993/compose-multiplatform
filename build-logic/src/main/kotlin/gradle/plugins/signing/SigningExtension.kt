package gradle.plugins.signing

import gradle.api.initialization.localProperties
import gradle.api.initialization.settingsProperties
import gradle.api.project.settings
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningExtension

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun SigningExtension.registerGPGTasks(): Unit = project.pluginManager.withPlugin("signing") {
    val passphrase = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.passphrase")
    }
    val keyType = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.key.type", "RSA")
    }
    val keyLength = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.key.length", "4096").toInt()
    }
    val subkeyType = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.subkey.type", "RSA")
    }
    val subkeyLength = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.subkey.length", "4096").toInt()
    }
    val nameReal = project.providers.provider {
        project.settings.localProperties.getProperty(
            "signing.gnupg.name.real",
            project.settings.settingsProperties.developer.name!!,
        )
    }
    val nameComment = project.providers.provider {
        project.settings.localProperties.getProperty(
            "signing.gnupg.name.comment",
            project.description.orEmpty(),
        )
    }
    val nameEmail = project.providers.provider {
        project.settings.localProperties.getProperty(
            "signing.gnupg.name.email",
            project.settings.settingsProperties.developer.email!!,
        )
    }
    val expireDate = project.providers.provider {
        project.settings.localProperties.getProperty("signing.gnupg.expiryDate", "0").toLong()
    }

    /** Distribute signing gpg key
     * There are 3 servers supported by Central servers: [ keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu ]
     */
    project.tasks.register<Exec>("gpgGenKey") {
        description = "Generates the signing GPG key"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-gen-key.sh").absolutePath

        args(
            passphrase,
            keyType,
            keyLength,
            subkeyType,
            subkeyLength,
            nameReal,
            nameComment,
            nameEmail,
            expireDate,
        )
    }

    project.tasks.register<Exec>("gpgKeyList") {
        description = "List the signing GPG keys"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-key-list.sh").absolutePath

        args(nameReal, passphrase)
    }

    project.tasks.register<Exec>("gpgCleanKeys") {
        description = "Clean the signing GPG keys"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-clean-keys.sh").absolutePath

        args(nameReal)
    }

    project.tasks.register<Exec>("gpgExportKey") {
        description = "Export the signing GPG key to servers: [keyserver.ubuntu.com, keys.openpgp.org, pgp.mit.edu]"
        group = "signing"

        executable = project.settings.settingsDir.resolve("scripts/gpg/gpg-export-key.sh").absolutePath

        args(
            project.providers.provider {
                project.settings.localProperties.getProperty("signing.gnupg.key")
                    ?: error("signing.gnupg.key missing in local.properties")
            },
        )
    }
}


