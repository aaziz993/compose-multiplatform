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
import org.gradle.plugins.signing.SigningExtension
import org.pgpainless.PGPainless
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
    keyFile: File = File(project.projectDir, "signing.gpg"),
    keyType: String = project.sensitiveOrElse("signing.gpg.key.type") { "RSA" },
    keyParam: String = project.sensitiveOrElse("signing.gpg.key.param") { "4096" },
    password: String = project.sensitive("signing.gpg.key.password"),
    subkeyType: String = project.sensitiveOrElse("signing.gpg.subkey.type") { "RSA" },
    subkeyParam: String = project.sensitiveOrElse("signing.gpg.subkey.param") { "4096" },
    nameReal: String = project.sensitiveOrElse("signing.gpg.name.real") {
        project.settings.settingsScript.developer.name!!
    },
    nameEmail: String = project.sensitiveOrElse("signing.gpg.name.email") {
        project.settings.settingsScript.developer.email!!
    },
    nameComment: String = project.sensitiveOrElse("signing.gpg.name.comment") { project.description.orEmpty() },
    expire: Long = project.sensitiveOrElse("signing.gpg.expire") { "0" }.toLong()
): Unit = project.pluginManager.withPlugin("signing") {
    val key = keyFile.takeIf(File::exists)?.readText()
        ?: PGPainless.modernKeyRing(
            keyType,
            keyParam,
            password,
            subkeyType,
            subkeyParam,
            nameReal,
            nameEmail,
            nameComment,
            expire,
        ).toAsciiArmored().also(keyFile::writeText)

    project.signing.useInMemoryPgpKeys(key, password)
}

private fun PGPainless.Companion.modernKeyRing(
    keyType: String = "RSA",
    keyParam: String = "4096",
    passphrase: String = "",
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

private fun String.toKeyType(arg: String): KeyType = when (uppercase()) {
    "EDDSA_LEGACY" -> KeyType.EDDSA_LEGACY(EdDSALegacyCurve.valueOf("_$arg"))
    "ECDSA" -> KeyType.ECDSA(EllipticCurve.valueOf("_$arg"))
    "XDH_LEGACY" -> KeyType.XDH_LEGACY(XDHLegacySpec.valueOf("_$arg"))
    "RSA" -> KeyType.RSA(RsaLength.valueOf("_$arg"))

    else -> throw IllegalArgumentException("Unknown key type: $this")
}

private fun PGPSecretKeyRing.toAsciiArmored(): String = ByteArrayOutputStream().run {
    ArmoredOutputStream(this).use { armor ->
        encode(armor)
    }
    toString(Charsets.UTF_8.name())
}


