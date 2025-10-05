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
import klib.data.type.primitives.string.decode
import js.buffer.ArrayBuffer
import js.date.Date
import js.objects.unsafeJso
import js.promise.Promise
import js.promise.catch
import js.typedarrays.Uint8Array
import js.typedarrays.toUint8Array

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

    return generateKey(
        unsafeJso {
            type = keyType
            curve?.let { this.curve = it }
            rsaBits?.let { rsaBits = it }
            subKeys.takeIf(List<*>::isNotEmpty)?.let {
                subkeys = it.map {
                    unsafeJso<SubkeyOptions> {
                        when (it.key) {
                            is ECC -> {
                                type = "ecc"
                                curve = CURVES[it.key.curve]
                            }

                            is RSA -> {
                                type = "rsa"
                                rsaBits = it.key.size.toDouble()
                            }

                            null -> {
                                type = keyType
                                this.curve = curve
                                this.rsaBits = rsaBits
                            }
                        }
                        sign = it.sign
                    }
                }.toTypedArray()
            }
            this.userIDs = userIDs.map { uid ->
                unsafeJso<UserID> {
                    uid.name?.let { name = it }
                    uid.comment?.let { comment = it }
                    uid.email?.let { email = it }
                }
            }.toTypedArray()
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
    ).then {
        if (armored) {
            (it.privateKey as String).encodeToByteArray()
        }
        else {
            (it.privateKey as Uint8Array<*>).toByteArray()
        }
    }.await()
}

public actual suspend fun ByteArray.armorPGPKey(): ByteArray = readKey().then { it.armor().encodeToByteArray() }.await()

public actual suspend fun ByteArray.dearmorPGPKey(): ByteArray = readKey().then { it.write().toByteArray() }.await()

public actual suspend fun ByteArray.pgpKeyMetadata(): PGPKeyMetadata = readKey().flatThen { k ->
    k.getExpirationTime().then {
        PGPKeyMetadata(
            fingerprint = k.getFingerprint(),
            userIDs = k.getUserIDs().map(PGPUserId::parse),
            createDate = k.getCreationTime().getTime().toLong(),
            expireDate = it?.let { if (it is Date) it.getTime().toLong() else 0 },
        )
    }
}.await()

private fun ByteArray.readPrivateKey(): Promise<PrivateKey> = readPrivateKey(
    unsafeJso {
        if (isPGPArmored) {
            armoredKey = decode()
        }
        else {
            binaryKey = toUint8Array()
        }
    },
)

public actual suspend fun ByteArray.privatePGPKeys(armored: Boolean): List<ByteArray> = readPrivateKeys(
    unsafeJso {
        if (isPGPArmored) {
            armoredKeys = decode()
        }
        else {
            binaryKeys = toUint8Array()
        }
    },
).then { it.map { if (armored) it.armor().encodeToByteArray() else it.write().toByteArray() } }.await()

private fun ByteArray.readKey(): Promise<Key> = readKey(
    unsafeJso {
        if (isPGPArmored) {
            armoredKey = decode()
        }
        else {
            binaryKey = toUint8Array()
        }
    },
)

public actual suspend fun ByteArray.publicPGPKey(armored: Boolean): ByteArray =
    readKey().then(Key::toPublic).then { if (armored) it.armor().encodeToByteArray() else it.write().toByteArray() }.await()

public actual suspend fun ByteArray.publicPGPKeys(armored: Boolean): List<ByteArray> = readKeys(
    unsafeJso {
        if (isPGPArmored) {
            armoredKeys = decode()
        }
        else {
            binaryKeys = toUint8Array()
        }
    },
).then { it.map { if (armored) it.armor().encodeToByteArray() else it.write().toByteArray() } }.await()

public actual suspend fun ByteArray.changePGPKeyPassword(
    vararg oldPasswords: String,
    password: String?,
    armored: Boolean,
): ByteArray = readPrivateKey().flatThen { k ->
    decryptKey(
        unsafeJso {
            privateKey = k
            passphrase = arrayOf(*oldPasswords)
        },
    )
}.flatThen { dk ->
    if (password == null) {
        Promise.resolve(dk)
    }
    else {
        encryptKey(
            unsafeJso {
                privateKey = dk
                passphrase = arrayOf(password)
            },
        )
    }
}.then {
    if (armored) it.armor().encodeToByteArray() else it.write().toByteArray()
}.await()

@Suppress("UNCHECKED_CAST")
public actual suspend fun ByteArray.revokePGPKey(
    vararg passwords: String,
    armored: Boolean,
): ByteArray = readPrivateKey().flatThen {
    if (it.isDecrypted()) {
        Promise.resolve(it)
    }
    else {
        decryptKey(
            unsafeJso {
                privateKey = it
                passphrase = arrayOf(*passphrase)
            },
        )
    }
}.then {
    revokeKey(
        unsafeJso {
            key = it
            format = if (armored) "armored" else "binary"
        },
    )
}.then { if (armored) (it as String).encodeToByteArray() else (it as Uint8Array<ArrayBuffer>).toByteArray() }.await()

private suspend fun List<ByteArray>.readDecryptedKeys(passwords: List<String>?): Array<PrivateKey> =
    map {
        it.readPrivateKey().flatThen {
            if (it.isDecrypted()) {
                Promise.resolve(it)
            }
            else {
                decryptKey(
                    unsafeJso {
                        privateKey = it
                        passphrase = passwords?.toTypedArray() ?: emptyArray()
                    },
                )
            }
        }.await()
    }.toTypedArray()

public actual suspend fun ByteArray.encryptPGP(
    encryptionKeys: List<ByteArray>,
    signingKeys: List<ByteArray>?,
    signingKeysPasswords: List<String>?,
    passwords: List<String>?,
    armored: Boolean,
    isText: Boolean,
): ByteArray = encrypt(
    unsafeJso {
        message = createMessage(
            unsafeJso {
                if (isText) {
                    text = this@encryptPGP.decodeToString()
                }
                else {
                    binary = this@encryptPGP
                }
            },
        ).await()
        this.encryptionKeys = encryptionKeys.map { it.readKey().await() }.toTypedArray()
        signingKeys?.let { this.signingKeys = signingKeys.readDecryptedKeys(signingKeysPasswords) }
        passwords?.let {
            this.passwords = it.toTypedArray()
        }
        format = if (armored) "armored" else "binary"
    },
).then { if (armored) (it as String).encodeToByteArray() else (it as Uint8Array<*>).toByteArray() }.await()

public actual suspend fun ByteArray.decryptPGP(
    decryptionKeys: List<ByteArray>,
    decryptionKeysPasswords: List<String>?,
    verificationKeys: List<ByteArray>?,
    passwords: List<String>?,
): PGPVerifiedResult = readMessage(
    unsafeJso {
        if (isPGPArmored) {
            armoredMessage = this@decryptPGP.decodeToString()
        }
        else {
            binaryMessage = this@decryptPGP
        }
    },
).await().let {
    decrypt(
        unsafeJso {
            this.decryptionKeys = decryptionKeys.readDecryptedKeys(decryptionKeysPasswords)
            verificationKeys?.let {
                this.verificationKeys = verificationKeys.map { it.readKey().await() }.toTypedArray()
            }
            passwords?.let { this.passwords = it.toTypedArray() }
        },
    ).await().let { dr ->
        dr.signatures.map { PGPVerification(it.keyID.toHex(), it.verified.catch { false }.await()) }.let {
            PGPVerifiedResult(
                (dr.data as String).encodeToByteArray(),
                { it },
            )
        }
    }
}

public actual suspend fun ByteArray.signPGP(
    signingKeys: List<ByteArray>,
    signingKeysPasswords: List<String>?,
    mode: PGPSignMode,
    detached: Boolean,
    armored: Boolean,
): ByteArray = sign(
    unsafeJso {
        message = when (mode) {
            PGPSignMode.BINARY -> createMessage(
                unsafeJso {
                    binary = this@signPGP
                },
            )

            PGPSignMode.TEXT -> createMessage(
                unsafeJso {
                    text = this@signPGP.decodeToString()
                },
            )

            PGPSignMode.CLEARTEXT_SIGN -> createCleartextMessage(
                unsafeJso {
                    text = decode()
                },
            )
        }.await()
        this.signingKeys = signingKeys.readDecryptedKeys(signingKeysPasswords)
        this.detached = detached
        format = if (armored) "armored" else "binary"
    },
).then { if (armored) (it as String).encodeToByteArray() else (it as Uint8Array<*>).toByteArray() }.await()

public actual suspend fun ByteArray.verifyPGP(
    verificationKeys: List<ByteArray>,
    mode: PGPSignMode,
    signatures: List<ByteArray>?,
): PGPVerifiedResult {
    require(signatures == null || signatures.size == 1) {
        "Only one or zero signatures can be used to verify"
    }

    return verify(
        unsafeJso {
            message = when (mode) {
                PGPSignMode.BINARY -> createMessage(
                    unsafeJso {
                        binary = this@verifyPGP
                    },
                )

                PGPSignMode.TEXT -> createMessage(
                    unsafeJso {
                        text = decode()
                    },
                )

                PGPSignMode.CLEARTEXT_SIGN -> createCleartextMessage(
                    unsafeJso {
                        text = decode()
                    },
                )
            }.await()
            signatures?.first()?.let { s ->
                signature = readSignature(
                    unsafeJso {
                        if (s.isPGPArmored) {
                            armoredSignature = s.decodeToString()
                        }
                        else {
                            binarySignature = s.toUint8Array()
                        }
                    },
                ).await()
            }
            this.verificationKeys = verificationKeys.map { it.readKey().await() }.toTypedArray()
        },
    ).await().let { vr ->
        vr.signatures.map { PGPVerification(it.keyID.toHex(), it.verified.catch { false }.await()) }.let {
            PGPVerifiedResult(
                if (mode == PGPSignMode.CLEARTEXT_SIGN) (vr.data as String).encodeToByteArray() else (vr.data as Uint8Array<*>).toByteArray(),
                { it },
            )
        }
    }
}
