package klib.data.cryptography

import dev.whyoleg.cryptography.serialization.asn1.BitArray
import dev.whyoleg.cryptography.serialization.asn1.Der
import dev.whyoleg.cryptography.serialization.asn1.ObjectIdentifier
import dev.whyoleg.cryptography.serialization.asn1.modules.PrivateKeyInfo
import dev.whyoleg.cryptography.serialization.asn1.modules.RSA
import dev.whyoleg.cryptography.serialization.asn1.modules.SubjectPublicKeyInfo
import dev.whyoleg.cryptography.serialization.asn1.modules.UnknownKeyAlgorithmIdentifier
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

public inline fun <reified T> T.encodeDerToByteArray(): ByteArray = Der.encodeToByteArray(this)

public inline fun <reified T> ByteArray.decodeDerToByteArray(): T = Der.decodeFromByteArray(this)

public fun ByteArray.encodeDerPublicRSAKey(): ByteArray =
    SubjectPublicKeyInfo(UnknownKeyAlgorithmIdentifier(ObjectIdentifier.RSA), BitArray(0, this)).encodeDerToByteArray()

public fun ByteArray.decodeDerPublicRSAKey(): SubjectPublicKeyInfo = decodeDerToByteArray()

public fun ByteArray.encodeDerPrivateRSAKey(version: Int): ByteArray =
    PrivateKeyInfo(version, UnknownKeyAlgorithmIdentifier(ObjectIdentifier.RSA), this).encodeDerToByteArray()

public fun ByteArray.decodeDerPrivateRSAKey(): PrivateKeyInfo = decodeDerToByteArray()
