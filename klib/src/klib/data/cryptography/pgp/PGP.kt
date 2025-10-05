package klib.data.cryptography.pgp

import klib.data.cryptography.pgp.model.ECC
import klib.data.cryptography.pgp.model.PGPKey
import klib.data.cryptography.pgp.model.PGPKeyMetadata
import klib.data.cryptography.pgp.model.PGPSignMode
import klib.data.cryptography.pgp.model.PGPSubKeyType
import klib.data.cryptography.pgp.model.PGPUserId
import klib.data.cryptography.pgp.model.PGPVerifiedResult
import klib.data.type.collections.startsWith

private val PGP_ARMOR_HEADER_START: ByteArray = "-----BEGIN PGP ".encodeToByteArray()

public fun ByteArray.isPGPArmored(): Boolean = startsWith(PGP_ARMOR_HEADER_START)

public expect suspend fun generatePGPKey(
    key: PGPKey = ECC(),
    subKeys: List<PGPSubKeyType> = emptyList(),
    userIDs: List<PGPUserId>,
    // Number of seconds from the key creation time after which the key expires.
    // null or 0 (never expires)
    expireDate: Long? = null,
    // The password used to encrypt the generated private key. If omitted or empty, the key won't be encrypted.
    password: String? = null,
    // Format of the output keys
    armored: Boolean = true,
): ByteArray

public expect suspend fun ByteArray.pgpKeyMetadata(): PGPKeyMetadata

public expect suspend fun ByteArray.armorPGPKey(): ByteArray

public expect suspend fun ByteArray.dearmorPGPKey(): ByteArray

public expect suspend fun ByteArray.privatePGPKeys(armored: Boolean = true): List<ByteArray>

public expect suspend fun ByteArray.publicPGPKey(armored: Boolean = true): ByteArray

public expect suspend fun ByteArray.publicPGPKeys(armored: Boolean = true): List<ByteArray>

public expect suspend fun ByteArray.changePGPKeyPassword(
    vararg oldPasswords: String,
    password: String? = null,
    armored: Boolean = true,
): ByteArray

public expect suspend fun ByteArray.revokePGPKey(
    vararg passwords: String,
    armored: Boolean = true,
): ByteArray

public expect suspend fun ByteArray.encryptPGP(
    encryptionKeys: List<ByteArray>, // Public keys
    signingKeys: List<ByteArray>? = null, // Private keys
    signingKeysPasswords: List<String>? = null, // Private keys passwords
    passwords: List<String>? = null,
    armored: Boolean = true,
    isText: Boolean = true,
): ByteArray

public expect suspend fun ByteArray.decryptPGP(
    decryptionKeys: List<ByteArray>, // Private keys
    decryptionKeysPasswords: List<String>? = null, // Private keys passwords
    verificationKeys: List<ByteArray>? = null, // Public keys
    passwords: List<String>? = null,
): PGPVerifiedResult

public expect suspend fun ByteArray.signPGP(
    signingKeys: List<ByteArray>,
    signingKeysPasswords: List<String>? = null,
    mode: PGPSignMode = PGPSignMode.CLEARTEXT_SIGN,
    detached: Boolean = false,
    armored: Boolean = true,
): ByteArray

public expect suspend fun ByteArray.verifyPGP(
    verificationKeys: List<ByteArray>,
    mode: PGPSignMode = PGPSignMode.CLEARTEXT_SIGN,
    signatures: List<ByteArray>? = null,
): PGPVerifiedResult

