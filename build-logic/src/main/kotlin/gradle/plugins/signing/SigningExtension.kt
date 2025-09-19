package gradle.plugins.signing

import gradle.api.initialization.settingsScript
import gradle.api.project.sensitive
import gradle.api.project.sensitiveOrElse
import gradle.api.project.settings
import gradle.api.project.signing
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Date
import org.bouncycastle.bcpg.ArmoredOutputStream
import org.bouncycastle.openpgp.PGPSecretKeyRing
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningExtension
import org.gradle.process.CommandLineArgumentProvider
import org.pgpainless.PGPainless
import org.pgpainless.PGPainless.Companion.buildKeyRing
import org.pgpainless.algorithm.KeyFlag
import org.pgpainless.key.generation.KeySpec.Companion.getBuilder
import org.pgpainless.key.generation.type.KeyType
import org.pgpainless.key.generation.type.ecc.EllipticCurve
import org.pgpainless.key.generation.type.eddsa_legacy.EdDSALegacyCurve
import org.pgpainless.key.generation.type.rsa.RsaLength
import org.pgpainless.key.generation.type.xdh_legacy.XDHLegacySpec
import org.pgpainless.util.Passphrase

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun SigningExtension.gpg(
    keyFile: File = File(project.projectDir, "signing.asc"),
    password: String = project.sensitive("signing.gnupg.password"),
    keyType: String = project.sensitiveOrElse("signing.gnupg.key.type") { "RSA" },
    keyParam: String = project.sensitiveOrElse("signing.gnupg.key.param") { "4096" },
    subkeyType: String = project.sensitiveOrElse("signing.gnupg.subkey.type") { "RSA" },
    subkeyParam: String = project.sensitiveOrElse("signing.gnupg.subkey.param") { "4096" },
    nameReal: String = project.sensitiveOrElse("signing.gnupg.name.real") {
        project.settings.settingsScript.developer.name!!
    },
    nameEmail: String = project.sensitiveOrElse("signing.gnupg.name.email") {
        project.settings.settingsScript.developer.email!!
    },
    nameComment: String = project.sensitiveOrElse("signing.gnupg.name.comment") { project.description.orEmpty() },
    expire: Long = project.sensitiveOrElse("signing.gnupg.expire") { "0" }.toLong()

): Unit = project.pluginManager.withPlugin("signing") {
    val key = keyFile.takeIf(File::exists)?.readText()
        ?: PGPainless.modernKeyRing(
            password,
            keyType,
            keyParam,
            subkeyType,
            subkeyParam,
            nameReal,
            nameEmail,
            nameComment,
            expire,
        ).toAsciiArmored().also(keyFile::writeText)

    project.signing.useInMemoryPgpKeys(key, password)
}

public fun PGPainless.Companion.modernKeyRing(
    passphrase: String = "",
    keyType: String = "RSA",
    keyParam: String = "4096",
    subkeyType: String = "RSA",
    subkeyParam: String = "4096",
    nameReal: String = "",
    nameEmail: String = "",
    nameComment: String = "",
    expire: Long = 0L
): PGPSecretKeyRing =
    buildKeyRing()
        .apply {
            setPrimaryKey(
                getBuilder(
                    keyType.toKeyType(keyParam), KeyFlag.CERTIFY_OTHER,
                ),
            )
            addSubkey(
                getBuilder(
                    subkeyType.toKeyType(subkeyParam),
                    KeyFlag.ENCRYPT_COMMS,
                    KeyFlag.ENCRYPT_STORAGE,
                ),
            )
            addSubkey(
                getBuilder(KeyType.EDDSA_LEGACY(EdDSALegacyCurve._Ed25519), KeyFlag.SIGN_DATA),
            )
            setPassphrase(Passphrase(passphrase.toCharArray()))

            if (nameEmail.isNotBlank()) {
                val userId = buildString {
                    append(nameReal)
                    if (nameComment.isNotBlank()) append(" ($nameComment)")
                    append(" <$nameEmail>")
                }
                addUserId(userId)
            }

            // Set expiration if > 0
            if (expire > 0L) {
                val expireDate = Date(System.currentTimeMillis() + expire * 1000)
                setExpirationDate(expireDate)
            }
        }
        .build()

public fun String.toKeyType(arg: String): KeyType = when (this) {
    "EDDSA_LEGACY" -> KeyType.EDDSA_LEGACY(EdDSALegacyCurve.valueOf("_$arg"))
    "ECDSA" -> KeyType.ECDSA(EllipticCurve.valueOf("_$arg"))
    "XDH_LEGACY" -> KeyType.XDH_LEGACY(XDHLegacySpec.valueOf("_$arg"))
    "RSA" -> KeyType.RSA(RsaLength.valueOf("_$arg"))

    else -> throw IllegalArgumentException("Unknown key type: $this")
}

public fun PGPSecretKeyRing.toAsciiArmored(): String = ByteArrayOutputStream().run {
    ArmoredOutputStream(this).use { armor ->
        encode(armor)
    }
    toString(Charsets.UTF_8.name())
}


