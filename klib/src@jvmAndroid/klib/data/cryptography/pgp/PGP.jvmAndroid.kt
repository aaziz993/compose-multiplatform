package klib.data.cryptography.pgp

import klib.data.cryptography.pgp.model.COMPRESSIONS
import klib.data.cryptography.model.Curve
import klib.data.cryptography.pgp.model.ECC
import klib.data.cryptography.pgp.model.HASH_ALGORITHMS
import klib.data.cryptography.pgp.model.PGPKey
import klib.data.cryptography.pgp.model.PGPKeyMetadata
import klib.data.cryptography.pgp.model.PGPSignMode
import klib.data.cryptography.pgp.model.PGPSubKeyType
import klib.data.cryptography.pgp.model.PGPUserId
import klib.data.cryptography.pgp.model.PGPVerification
import klib.data.cryptography.pgp.model.PGPVerifiedResult
import klib.data.cryptography.pgp.model.RSA
import klib.data.cryptography.pgp.model.SYMMETRIC_ALGORITHMS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.openpgp.PGPException
import org.bouncycastle.openpgp.PGPKeyRing
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection
import org.bouncycastle.openpgp.PGPRuntimeOperationException
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection
import org.pgpainless.PGPainless
import org.pgpainless.algorithm.KeyFlag
import org.pgpainless.decryption_verification.ConsumerOptions
import org.pgpainless.decryption_verification.SignatureVerification
import org.pgpainless.exception.MalformedOpenPgpMessageException
import org.pgpainless.exception.MissingDecryptionMethodException
import org.pgpainless.exception.WrongPassphraseException
import org.pgpainless.key.generation.KeySpec
import org.pgpainless.key.generation.KeySpecBuilder
import org.pgpainless.key.generation.type.KeyType
import org.pgpainless.key.generation.type.ecc.EllipticCurve
import org.pgpainless.key.generation.type.ecc.ecdh.ECDH
import org.pgpainless.key.generation.type.ecc.ecdsa.ECDSA
import org.pgpainless.key.generation.type.rsa.RsaLength
import org.pgpainless.key.info.KeyRingInfo
import org.pgpainless.key.util.UserId
import org.pgpainless.sop.MatchMakingSecretKeyRingProtector
import org.pgpainless.sop.SOPImpl
import org.pgpainless.util.ArmoredOutputStreamFactory
import org.pgpainless.util.Passphrase
import sop.SOP
import sop.enums.InlineSignAs
import sop.exception.SOPGPException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import org.bouncycastle.openpgp.PGPSecretKeyRing
import java.util.Date
import klib.data.type.collections.map.getStrict
import klib.data.type.primitives.string.encodeToByteArray
import kotlinx.coroutines.IO
import org.bouncycastle.openpgp.api.OpenPGPKey
import org.pgpainless.bouncycastle.extensions.encode
import org.pgpainless.key.generation.type.eddsa_legacy.EdDSALegacyCurve
import org.pgpainless.key.generation.type.xdh_legacy.XDHLegacySpec
import org.pgpainless.key.parsing.KeyRingReader

private var sop: SOP = SOPImpl()

private fun InputStream.readSecretKeys(requireContent: Boolean): PGPSecretKeyRingCollection {
    val keys =
        try {
            KeyRingReader.readSecretKeyRingCollection(this)
        }
        catch (e: IOException) {
            if (e.message == null) {
                throw e
            }
            if (e.message!!.startsWith("unknown object in stream:") ||
                e.message!!.startsWith("invalid header encountered")
            ) {
                throw SOPGPException.BadData(e)
            }
            throw e
        }
    if (requireContent && keys.none()) {
        throw SOPGPException.BadData(PGPException("No key data found."))
    }

    return keys
}

private fun InputStream.readPublicKeys(requireContent: Boolean): PGPPublicKeyRingCollection {
    val certs =
        try {
            KeyRingReader.readKeyRingCollection(this, false)
        }
        catch (e: IOException) {
            if (e.message == null) {
                throw e
            }
            if (e.message!!.startsWith("unknown object in stream:") ||
                e.message!!.startsWith("invalid header encountered")
            ) {
                throw SOPGPException.BadData(e)
            }
            throw e
        }
        catch (e: PGPRuntimeOperationException) {
            throw SOPGPException.BadData(e)
        }

    if (certs.pgpSecretKeyRingCollection.any()) {
        throw SOPGPException.BadData(
            "Secret key components encountered, while certificates were expected.",
        )
    }

    if (requireContent && certs.pgpPublicKeyRingCollection.none()) {
        throw SOPGPException.BadData(PGPException("No cert data found."))
    }
    return certs.pgpPublicKeyRingCollection
}

private fun PGPKey.toKeySpecBuilders(
    isSubKeyOption: Boolean = false,
    sign: Boolean = false,
): List<KeySpecBuilder> =
    when (this) {
        is RSA ->
            KeyType
                .RSA(RsaLength.entries.single { it.length == size })
                .let { listOf(it, it) }

        is ECC ->
            when (curve) {
                Curve.CURVE25519, Curve.ED25519 -> {
                    listOf(
                        KeyType.EDDSA_LEGACY(EdDSALegacyCurve._Ed25519),
                        KeyType.XDH_LEGACY(XDHLegacySpec._X25519),
                    )
                }

                Curve.P256, Curve.P384, Curve.P521,
                Curve.BRAINPOOLP256R1, Curve.BRAINPOOLP384R1, Curve.BRAINPOOLP512R1,
                Curve.SECP256K1,
                    -> {
                    val curve = EllipticCurve.entries.single { it.name == curve.name }
                    listOf(
                        ECDSA.fromCurve(curve),
                        ECDH.fromCurve(curve),
                    )
                }
            }
    }.let {
        if (isSubKeyOption) {
            if (sign) {
                listOf(KeySpec.getBuilder(it[0], KeyFlag.SIGN_DATA))
            }
            else {
                listOf(
                    KeySpec.getBuilder(
                        it[1],
                        KeyFlag.ENCRYPT_STORAGE,
                        KeyFlag.ENCRYPT_COMMS,
                    ),
                )
            }
        }
        else {
            listOf(
                KeySpec.getBuilder(
                    it[0],
                    KeyFlag.CERTIFY_OTHER,
                    KeyFlag.SIGN_DATA,
                ),
                KeySpec.getBuilder(
                    it[1],
                    KeyFlag.ENCRYPT_STORAGE,
                    KeyFlag.ENCRYPT_COMMS,
                ),
            )
        }.onEach { ks ->
            compressionAlgorithms?.let { compressionAlgorithms ->
                ks.overridePreferredCompressionAlgorithms(
                    *compressionAlgorithms
                        .map(COMPRESSIONS::getStrict)
                        .toTypedArray(),
                )
            }

            hashAlgorithms?.let { hashAlgorithms ->
                ks.overridePreferredHashAlgorithms(
                    *hashAlgorithms
                        .map(HASH_ALGORITHMS::getStrict).toTypedArray(),
                )
            }

            symmetricKeyAlgorithms?.let { symmetricKeyAlgorithms ->
                ks.overridePreferredSymmetricKeyAlgorithms(
                    *symmetricKeyAlgorithms
                        .map(SYMMETRIC_ALGORITHMS::getStrict)
                        .toTypedArray(),
                )
            }
        }
    }

private fun PGPKeyRing.toArmoredByteArray(): ByteArray = ByteArrayOutputStream().apply {
    val armoredOutputStream = ArmoredOutputStreamFactory.get(this)
    encode(armoredOutputStream)
    armoredOutputStream.close()
}.toByteArray()

public actual suspend fun generatePGPKey(
    key: PGPKey,
    subKeys: List<PGPSubKeyType>,
    userIDs: List<PGPUserId>,
    expireDate: Long?,
    password: String?,
    armored: Boolean,
): ByteArray {
    try {
        return PGPainless.getInstance()
            .buildKey()
            .also { builder ->
                key.toKeySpecBuilders().let {
                    builder.setPrimaryKey(it[0]).addSubkey(it[1])
                }

                subKeys.forEach {
                    builder.addSubkey(
                        (it.key ?: key).toKeySpecBuilders(
                            true,
                            it.sign,
                        )[0],
                    )
                }

                if (password != null) builder.setPassphrase(Passphrase.fromPassword(password))

                userIDs.forEach { uid ->
                    builder.addUserId(
                        UserId.builder()
                            .also { uidBuilder ->
                                uid.name?.let { uidBuilder.withName(it) }
                                uid.comment?.let { uidBuilder.withComment(it) }
                                uid.email?.let { uidBuilder.withEmail(it) }
                            }.build(),
                    )
                }

                builder.setExpirationDate(
                    expireDate?.takeIf { it != 0L }?.let { Date(it * 1000L) },
                )
            }.build()
            .let { k -> if (armored) k.toAsciiArmoredString().encodeToByteArray() else k.encoded }
    }
    catch (e: InvalidAlgorithmParameterException) {
        throw SOPGPException.UnsupportedAsymmetricAlgo("Unsupported asymmetric algorithm.", e)
    }
    catch (e: NoSuchAlgorithmException) {
        throw SOPGPException.UnsupportedAsymmetricAlgo("Unsupported asymmetric algorithm.", e)
    }
    catch (e: PGPException) {
        throw RuntimeException(e)
    }
}

private fun KeyRingInfo.toPGPKeyMetadata(): PGPKeyMetadata =
    PGPKeyMetadata(
        fingerprint.toString(),
        userIds.map(PGPUserId::parse),
        creationDate.time,
        primaryKeyExpirationDate?.time ?: 0,
    )

public actual suspend fun ByteArray.pgpKeyMetadata(): PGPKeyMetadata =
    KeyRingInfo(
        PGPainless.getInstance().readKey().parseKey(this)!!,
    ).toPGPKeyMetadata()

public actual suspend fun ByteArray.armorPGPKey(): ByteArray =
    ByteArrayOutputStream().apply {
        sop.armor()!!
            .data(ByteArrayInputStream(this@armorPGPKey))
            .writeTo(this)
    }.toByteArray()

public actual suspend fun ByteArray.dearmorPGPKey(): ByteArray =
    ByteArrayOutputStream().apply {
        sop.dearmor()!!
            .data(ByteArrayInputStream(this@dearmorPGPKey))
            .writeTo(this)
    }.toByteArray()

public actual suspend fun ByteArray.privatePGPKeys(armored: Boolean): List<ByteArray> =
    ByteArrayInputStream(this).readSecretKeys(false).let { keyRings ->
        if (armored) keyRings.map(PGPSecretKeyRing::toArmoredByteArray)
        else keyRings.map(PGPSecretKeyRing::getEncoded)
    }

public actual suspend fun ByteArray.publicPGPKey(armored: Boolean): ByteArray =
    ByteArrayOutputStream().apply {
        sop.extractCert()!!
            .let { cert ->
                if (armored) cert else cert.noArmor()
            }.key(this@publicPGPKey)
            .writeTo(this)
    }.toByteArray()

public actual suspend fun ByteArray.publicPGPKeys(armored: Boolean): List<ByteArray> =
    ByteArrayInputStream(this).readPublicKeys(false).let { keyRings ->
        if (armored) keyRings.map(PGPKeyRing::toArmoredByteArray)
        else keyRings.map(PGPKeyRing::getEncoded)
    }

public actual suspend fun ByteArray.changePGPKeyPassword(
    vararg oldPasswords: String,
    password: String?,
    armored: Boolean,
): ByteArray =
    ByteArrayOutputStream().apply {
        sop.changeKeyPassword()!!
            .let { password ->
                if (armored) password
                else password.noArmor()
            }.let { password ->
                oldPasswords.fold(password) { cp, p -> cp.oldKeyPassphrase(p) }
            }.let {
                if (password == null) it
                else it.newKeyPassphrase(password)
            }.keys(ByteArrayInputStream(this@changePGPKeyPassword))
            .writeTo(this)
    }.toByteArray()

// one or more secret keys
public actual suspend fun ByteArray.revokePGPKey(
    vararg passwords: String, // primary key password(s) if the key(s) are protected
    armored: Boolean,
): ByteArray =
    ByteArrayOutputStream().apply {
        sop.revokeKey()!!
            .let { key ->
                if (armored) key else key.noArmor()
            }.let {
                passwords
                    .fold(it) { rk, p ->
                        rk.withKeyPassword(p)
                    }.keys(ByteArrayInputStream(this@revokePGPKey))
            }.writeTo(this)
    }.toByteArray()

public actual suspend fun ByteArray.encryptPGP(
    encryptionKeys: List<ByteArray>, // It does not matter, if the certificate is ASCII armored or not
    signingKeys: List<ByteArray>?, // Optionally: Sign the message
    signingKeysPasswords: List<String>?, // if signing key is protected
    passwords: List<String>?,
    armored: Boolean,
    isText: Boolean,
): ByteArray =
    ByteArrayOutputStream().apply {
        sop.encrypt()!!
            .let { encrypt ->
                if (armored) encrypt else encrypt.noArmor()
            }.let {
                // encrypt for each recipient
                encryptionKeys.fold(it) { e, k -> e.withCert(ByteArrayInputStream(k)) }
            }.let {
                signingKeys?.fold(it) { e, k -> e.signWith(ByteArrayInputStream(k)) } ?: it
            }.let {
                signingKeysPasswords?.fold(it) { e, p -> e.withKeyPassword(p) } ?: it
            }.let {
                passwords?.fold(it) { e, p -> e.withPassword(p) } ?: it
            }.plaintext(ByteArrayInputStream(this@encryptPGP))
            .writeTo(this)
    }.toByteArray()

@OptIn(ExperimentalStdlibApi::class)
private fun SignatureVerification.toPGPVerification(vkrc: List<PGPPublicKeyRingCollection>): PGPVerification =
    PGPVerification(
        signature.keyID.toHexString(),
        vkrc.any { keys ->
            keys.any { key ->
                key.publicKey.keyID == signingKey.certificateIdentifier.keyId &&
                    key.getPublicKey(signingKey.componentKeyIdentifier.keyId) != null
            }
        },
    )

public actual suspend fun ByteArray.decryptPGP(
    decryptionKeys: List<ByteArray>,
    decryptionKeysPasswords: List<String>?, // if decryption key is protected
    verificationKeys: List<ByteArray>?,
    passwords: List<String>?,
): PGPVerifiedResult {
    val consumerOptions = ConsumerOptions.get()
    val protector = MatchMakingSecretKeyRingProtector()

    decryptionKeys.forEach { decryptionKey ->
        ByteArrayInputStream(decryptionKey)
            .readSecretKeys(true).map(PGPainless.getInstance()::toKey).forEach { secretKey ->
                protector.addSecretKey(secretKey)
                consumerOptions.addDecryptionKey(secretKey, protector)
            }
    }

    decryptionKeysPasswords?.forEach {
        protector.addPassphrase(Passphrase.fromPassword(it))
    }

    val vkrc = verificationKeys?.map { ByteArrayInputStream(it).readPublicKeys(true) }.orEmpty()
        .onEach { keyRings ->
            keyRings.map(PGPainless.getInstance()::toCertificate)
                .forEach(consumerOptions::addVerificationCert)
        }

    passwords?.forEach { password ->
        consumerOptions.addMessagePassphrase(Passphrase.fromPassword(password))
        password.trimEnd().let {
            if (it != password) {
                consumerOptions.addMessagePassphrase(Passphrase.fromPassword(it))
            }
        }
    }

    if (consumerOptions.getDecryptionKeys().isEmpty() &&
        consumerOptions.getDecryptionPassphrases().isEmpty() &&
        consumerOptions.getSessionKey() == null
    ) {
        throw SOPGPException.MissingArg("Missing decryption key, passphrase or session key.")
    }

    val decryptionStream =
        try {
            PGPainless.getInstance()
                .processMessage()
                .onInputStream(ByteArrayInputStream(this))
                .withOptions(consumerOptions)
        }
        catch (e: MissingDecryptionMethodException) {
            throw SOPGPException.CannotDecrypt(
                "No usable decryption key or password provided.",
                e,
            )
        }
        catch (_: WrongPassphraseException) {
            throw SOPGPException.KeyIsProtected()
        }
        catch (e: MalformedOpenPgpMessageException) {
            throw SOPGPException.BadData(e)
        }
        catch (e: PGPException) {
            throw SOPGPException.BadData(e)
        }
        catch (e: IOException) {
            throw SOPGPException.BadData(e)
        }
        finally {
            // Forget passphrases after decryption
            protector.clear()
        }

    return PGPVerifiedResult(
        withContext(Dispatchers.IO) {
            decryptionStream.readAllBytes()
        },
    ) {
        val metadata = decryptionStream.metadata
        if (!metadata.isEncrypted) {
            throw SOPGPException.BadData("Data is not encrypted.")
        }

        metadata.verifiedInlineSignatures.map { it.toPGPVerification(vkrc) }
    }
}

private fun PGPSignMode.toSignMode(): InlineSignAs =
    when (this) {
        PGPSignMode.BINARY -> InlineSignAs.binary
        PGPSignMode.TEXT -> InlineSignAs.text
        PGPSignMode.CLEARTEXT_SIGN -> InlineSignAs.clearsigned
    }

public actual suspend fun ByteArray.signPGP(
    signingKeys: List<ByteArray>,
    signingKeysPasswords: List<String>?,
    mode: PGPSignMode,
    detached: Boolean,
    armored: Boolean,
): ByteArray = if (detached) pgpDetachedSign(signingKeys, signingKeysPasswords, armored)
else ByteArrayOutputStream().apply {
    sop.inlineSign()!!
        .mode(mode.toSignMode())
        .let { sign ->
            if (armored) sign
            else sign.noArmor()
        }.let {
            signingKeys.fold(it) { s, k -> s.key(ByteArrayInputStream(k)) }
        }.let {
            signingKeysPasswords?.fold(it) { s, p -> s.withKeyPassword(p) } ?: it
        }.data(ByteArrayInputStream(this@signPGP)).writeTo(this)
}.toByteArray()

private fun ByteArray.pgpDetachedSign(
    signingKeys: List<ByteArray>,
    signingKeysPasswords: List<String>?,
    armored: Boolean,
): ByteArray = ByteArrayOutputStream().apply {
    sop.detachedSign()!!
        .let { sign ->
            if (armored) sign else sign.noArmor()
        }.let {
            signingKeys.fold(it) { s, k -> s.key(ByteArrayInputStream(k)) }
        }.let {
            signingKeysPasswords?.fold(it) { s, p -> s.withKeyPassword(p) } ?: it
        }.data(ByteArrayInputStream(this@pgpDetachedSign)).writeTo(this)
}.toByteArray()

public actual suspend fun ByteArray.verifyPGP(
    verificationKeys: List<ByteArray>,
    mode: PGPSignMode,
    signatures: List<ByteArray>?
): PGPVerifiedResult =
    try {
        val options = ConsumerOptions.get()

        val vkrc = verificationKeys.map { key -> ByteArrayInputStream(key).readPublicKeys(true) }
            .onEach { keyRings ->
                keyRings.map(PGPainless.getInstance()::toCertificate)
                    .forEach(options::addVerificationCert)
            }

        signatures?.forEach { options.addVerificationOfDetachedSignatures(ByteArrayInputStream(it)) }

        val verificationStream = PGPainless.getInstance()
            .processMessage()
            .onInputStream(ByteArrayInputStream(this))
            .withOptions(options)

        PGPVerifiedResult(
            withContext(Dispatchers.IO) {
                verificationStream.readAllBytes()
            },
        ) {
            val result = verificationStream.metadata
            val verifications =
                if (result.isUsingCleartextSignatureFramework || !signatures.isNullOrEmpty()) {
                    result.verifiedDetachedSignatures
                }
                else {
                    result.verifiedInlineSignatures
                }

            if (options.getCertificateSource().getExplicitCertificates().isNotEmpty() &&
                verifications.isEmpty()
            ) throw SOPGPException.NoSignature()

            verifications.map { it.toPGPVerification(vkrc) }
        }
    }
    catch (e: MissingDecryptionMethodException) {
        throw SOPGPException.BadData("Cannot verify encrypted message.", e)
    }
    catch (e: MalformedOpenPgpMessageException) {
        throw SOPGPException.BadData(e)
    }
    catch (e: PGPException) {
        throw SOPGPException.BadData(e)
    }
