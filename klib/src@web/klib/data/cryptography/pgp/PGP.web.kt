@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.cryptography.pgp

import klib.data.cryptography.pgp.model.COMPRESSIONS
import klib.data.cryptography.pgp.model.CURVES
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
import klib.data.cryptography.pgp.model.SYMMETRIC_ALGORITHM_MAP
import klib.data.type.primitives.string.decodeToString
import js.buffer.ArrayBuffer
import js.date.Date
import js.objects.unsafeJso
import js.promise.catch
import js.typedarrays.Uint8Array
import js.typedarrays.toByteArray
import js.typedarrays.toUint8Array
import klib.data.cryptography.MIN_RSA_KEY_SIZE
import klib.data.type.await
import klib.data.type.collections.flatten
import klib.data.type.collections.takeIfNotEmpty
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsString
import kotlin.js.Promise
import kotlin.js.toBoolean
import kotlin.js.toJsArray
import kotlin.js.toJsBoolean
import kotlin.js.toJsString
import kotlin.js.toList

public actual suspend fun generatePGPKey(
    key: PGPKey,
    subKeys: List<PGPSubKeyType>,
    userIDs: List<PGPUserId>,
    expireDate: Long?,
    password: String?,
    armored: Boolean,
): ByteArray {
    require(userIDs.isNotEmpty()) {
        "UserIDs are required for key generation"
    }

    val keyType: String
    var curve: String? = null
    var rsaBits: Double? = null

    when (key) {
        is ECC -> {
            keyType = "ecc"
            curve = CURVES[key.curve]!!
        }

        is RSA -> {
            require(key.size >= MIN_RSA_KEY_SIZE) {
                "RSA size should be at least ${MIN_RSA_KEY_SIZE}, got: ${key.size}"
            }

            keyType = "rsa"
            rsaBits = key.size.toDouble()
        }
    }

    return openpgp.generateKey(
        unsafeJso {
            type = keyType
            curve?.let { this.curve = it }
            rsaBits?.let { rsaBits = it }
            subKeys.takeIfNotEmpty()?.let { subKeys ->
                this.subkeys = subKeys.map { subKey ->
                    unsafeJso<SubkeyOptions> {
                        when (subKey.key) {
                            is ECC -> {
                                type = "ecc"
                                curve = CURVES[subKey.key.curve]
                            }

                            is RSA -> {
                                type = "rsa"
                                rsaBits = subKey.key.size.toDouble()
                            }

                            null -> {
                                type = keyType
                                this.curve = curve
                                this.rsaBits = rsaBits
                            }
                        }
                        sign = subKey.sign
                    }
                }.toJsArray()
            }
            this.userIDs = userIDs.map { userId ->
                unsafeJso<UserID> {
                    userId.name?.let { name = it }
                    userId.comment?.let { comment = it }
                    userId.email?.let { email = it }
                }
            }.toJsArray()
            expireDate?.let { keyExpirationTime = it.toDouble() }
            password?.let { passphrase = it }
            config = unsafeJso {
                key.compressionAlgorithms?.let {
                    require(it.size == 1) {
                        "Only one compression algorithm allowed"
                    }
                    preferredCompressionAlgorithm = COMPRESSIONS[it.single()]!!
                }
                key.hashAlgorithms?.let {
                    require(it.size == 1) {
                        "Only one symmetric algorithm allowed"
                    }
                    preferredHashAlgorithm = HASH_ALGORITHMS[it.single()]!!
                }
                key.symmetricKeyAlgorithms?.let {
                    require(it.size == 1) {
                        "Only one symmetric algorithm allowed"
                    }
                    preferredSymmetricAlgorithm = SYMMETRIC_ALGORITHM_MAP[it.single()]!!
                }
            }
            format = if (armored) "armored" else "binary"
        },
    ).await().let { generatedKey ->
        if (armored) (generatedKey.privateKey as JsString).toString().encodeToByteArray()
        else (generatedKey.privateKey as Uint8Array<*>).toByteArray()
    }
}

public actual suspend fun ByteArray.armorPGPKey(): ByteArray = readKey().armor().encodeToByteArray()

public actual suspend fun ByteArray.dearmorPGPKey(): ByteArray = readKey().write().toByteArray()

public actual suspend fun ByteArray.pgpKeyMetadata(): PGPKeyMetadata = readKey().let { key ->
    val expireDate = key.getExpirationTime().await()
    PGPKeyMetadata(
        fingerprint = key.getFingerprint(),
        userIDs = key.getUserIDs().toList().map(JsString::toString).map(PGPUserId::parse),
        createDate = key.getCreationTime().getTime().toLong(),
        expireDate = expireDate?.let { if (it is Date) it.getTime().toLong() else 0 },
    )

}

private fun ByteArray.readPrivateKey(): Promise<PrivateKey> = openpgp.readPrivateKey(
    unsafeJso {
        if (isPGPArmored()) {
            armoredKey = decodeToString()
        }
        else {
            binaryKey = toUint8Array()
        }
    },
)

public actual suspend fun ByteArray.privatePGPKeys(armored: Boolean): List<ByteArray> = openpgp.readPrivateKeys(
    unsafeJso {
        if (isPGPArmored()) {
            armoredKeys = decodeToString()
        }
        else {
            binaryKeys = toUint8Array()
        }
    },
).await().let { privateKeys -> privateKeys.toList().map { privateKey -> if (armored) privateKey.armor().encodeToByteArray() else privateKey.write().toByteArray() } }

private suspend fun ByteArray.readKey(): Key = openpgp.readKey(
    unsafeJso {
        if (isPGPArmored()) {
            armoredKey = decodeToString()
        }
        else {
            binaryKey = toUint8Array()
        }
    },
).await()

public actual suspend fun ByteArray.publicPGPKey(armored: Boolean): ByteArray =
    readKey().toPublic().let { key -> if (armored) key.armor().encodeToByteArray() else key.write().toByteArray() }

public actual suspend fun ByteArray.publicPGPKeys(armored: Boolean): List<ByteArray> = openpgp.readKeys(
    unsafeJso {
        if (isPGPArmored()) {
            armoredKeys = decodeToString()
        }
        else {
            binaryKeys = toUint8Array()
        }
    },
).await().let { keys -> keys.toList().map { key -> if (armored) key.armor().encodeToByteArray() else key.write().toByteArray() } }

public actual suspend fun ByteArray.changePGPKeyPassword(
    vararg oldPasswords: String,
    password: String?,
    armored: Boolean,
): ByteArray = readPrivateKey().await().let { privateKey ->
    openpgp.decryptKey(
        unsafeJso {
            this.privateKey = privateKey
            passphrase = oldPasswords.map(String::toJsString).toJsArray()
        },
    )
}.await().let { decryptedKey ->
    if (password == null) decryptedKey
    else openpgp.encryptKey(
        unsafeJso {
            privateKey = decryptedKey
            passphrase = listOf(password.toJsString()).toJsArray()
        },
    ).await()
}.let { key ->
    if (armored) key.armor().encodeToByteArray() else key.write().toByteArray()
}

@Suppress("UNCHECKED_CAST")
public actual suspend fun ByteArray.revokePGPKey(
    vararg passwords: String,
    armored: Boolean,
): ByteArray = readPrivateKey().await().let { privateKey ->
    if (privateKey.isDecrypted()) privateKey
    else openpgp.decryptKey(
        unsafeJso {
            this.privateKey = privateKey
            passphrase = passwords.map(String::toJsString).toJsArray()
        },
    ).await()
}.let { key ->
    openpgp.revokeKey(
        unsafeJso {
            this.key = key
            format = if (armored) "armored" else "binary"
        },
    ).await()
}.let { key -> if (armored) (key as JsString).toString().encodeToByteArray() else (key as Uint8Array<*>).toByteArray() }

private suspend fun List<ByteArray>.readDecryptedKeys(passwords: List<String>?): Array<PrivateKey> =
    map {
        it.readPrivateKey().await().let { privateKey ->
            if (privateKey.isDecrypted()) privateKey
            else openpgp.decryptKey(
                unsafeJso {
                    this.privateKey = privateKey
                    passphrase = passwords.orEmpty().map(String::toJsString).toJsArray()
                },
            ).await()
        }
    }.toTypedArray()

public actual suspend fun ByteArray.encryptPGP(
    encryptionKeys: List<ByteArray>,
    signingKeys: List<ByteArray>?,
    signingKeysPasswords: List<String>?,
    passwords: List<String>?,
    armored: Boolean,
    isText: Boolean,
): ByteArray = openpgp.encrypt(
    unsafeJso {
        message = openpgp.createMessage(
            unsafeJso {
                if (isText) {
                    text = decodeToString()
                }
                else {
                    binary = toUint8Array()
                }
            },
        ).await()
        this.encryptionKeys = encryptionKeys.map { encryptionKey -> encryptionKey.readKey() }.toJsArray()
        signingKeys?.let { this.signingKeys = signingKeys.readDecryptedKeys(signingKeysPasswords).toJsArray() }
        passwords?.let {
            this.passwords = it.map(String::toJsString).toJsArray()
        }
        format = if (armored) "armored" else "binary"
    },
).await().let { encrypted -> if (armored) (encrypted as JsString).toString().encodeToByteArray() else (encrypted as Uint8Array<*>).toByteArray() }

public actual suspend fun ByteArray.decryptPGP(
    decryptionKeys: List<ByteArray>,
    decryptionKeysPasswords: List<String>?,
    verificationKeys: List<ByteArray>?,
    passwords: List<String>?,
): PGPVerifiedResult = openpgp.readMessage(
    unsafeJso {
        if (isPGPArmored()) {
            armoredMessage = decodeToString()
        }
        else {
            binaryMessage = toUint8Array()
        }
    },
).await().let {
    openpgp.decrypt(
        unsafeJso {
            this.decryptionKeys = decryptionKeys.readDecryptedKeys(decryptionKeysPasswords).toJsArray()
            verificationKeys?.let {
                this.verificationKeys = verificationKeys.map { verificationKey -> verificationKey.readKey() }.toJsArray()
            }
            passwords?.let { this.passwords = it.map(String::toJsString).toJsArray() }
        },
    ).await().let { decryptVerifyMessageResult ->
        decryptVerifyMessageResult.signatures.toList().map { result -> PGPVerification(result.keyID.toHex(), result.verified.catch { false.toJsBoolean() }.await().toBoolean()) }.let { verifications ->
            PGPVerifiedResult(
                (decryptVerifyMessageResult.data as JsString).toString().encodeToByteArray(),
            ) { verifications }
        }
    }
}

public actual suspend fun ByteArray.signPGP(
    signingKeys: List<ByteArray>,
    signingKeysPasswords: List<String>?,
    mode: PGPSignMode,
    detached: Boolean,
    armored: Boolean,
): ByteArray = openpgp.sign(
    unsafeJso {
        message = when (mode) {
            PGPSignMode.BINARY -> openpgp.createMessage(
                unsafeJso {
                    binary = toUint8Array()
                },
            )

            PGPSignMode.TEXT -> openpgp.createMessage(
                unsafeJso {
                    text = decodeToString()
                },
            )

            PGPSignMode.CLEARTEXT_SIGN -> openpgp.createCleartextMessage(
                unsafeJso {
                    text = decodeToString()
                },
            )
        }.await()
        this.signingKeys = signingKeys.readDecryptedKeys(signingKeysPasswords).toJsArray()
        this.detached = detached
        format = if (armored) "armored" else "binary"
    },
).await().let { signed -> if (armored) (signed as JsString).toString().encodeToByteArray() else (signed as Uint8Array<*>).toByteArray() }

public actual suspend fun ByteArray.verifyPGP(
    verificationKeys: List<ByteArray>,
    mode: PGPSignMode,
    signatures: List<ByteArray>?,
): PGPVerifiedResult {
    require(signatures == null || signatures.size == 1) {
        "Only one or zero signatures can be used to verify"
    }

    return openpgp.verify(
        unsafeJso {
            message = when (mode) {
                PGPSignMode.BINARY -> openpgp.createMessage(
                    unsafeJso {
                        binary = toUint8Array()
                    },
                )

                PGPSignMode.TEXT -> openpgp.createMessage(
                    unsafeJso {
                        text = decodeToString()
                    },
                )

                PGPSignMode.CLEARTEXT_SIGN -> openpgp.createCleartextMessage(
                    unsafeJso {
                        text = decodeToString()
                    },
                )
            }.await()

            signatures?.first()?.let { signature ->
                this.signature = openpgp.readSignature(
                    unsafeJso {
                        if (signature.isPGPArmored()) {
                            armoredSignature = signature.decodeToString()
                        }
                        else {
                            binarySignature = signature.toUint8Array()
                        }
                    },
                ).await()
            }
            this.verificationKeys = verificationKeys.map { verificationKey -> verificationKey.readKey() }.toJsArray()
        },
    ).await().let { decryptVerifyMessageResult ->
        decryptVerifyMessageResult.signatures.toList().map { result -> PGPVerification(result.keyID.toHex(), result.verified.catch { false.toJsBoolean() }.await().toBoolean()) }.let { verifications ->
            PGPVerifiedResult(
                if (mode == PGPSignMode.CLEARTEXT_SIGN) (decryptVerifyMessageResult.data as JsString).toString().encodeToByteArray() else (decryptVerifyMessageResult.data as Uint8Array<*>).toByteArray(),
            ) { verifications }
        }
    }
}
