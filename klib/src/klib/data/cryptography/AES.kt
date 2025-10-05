package klib.data.cryptography

import dev.whyoleg.cryptography.BinarySize
import dev.whyoleg.cryptography.BinarySize.Companion.bits
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.materials.key.KeyDecoder
import dev.whyoleg.cryptography.materials.key.KeyGenerator

private val AES_GCM: AES.GCM by lazy { CryptographyProvider.Default.get(AES.GCM) }

private fun aesGcmKeyGenerator(keySize: BinarySize = AES.Key.Size.B256): KeyGenerator<AES.GCM.Key> = AES_GCM.keyGenerator(keySize)

public val AES_GCM_B128: KeyGenerator<AES.GCM.Key> by lazy {
    aesGcmKeyGenerator(AES.Key.Size.B128)
}

public val AES_GCM_B192: KeyGenerator<AES.GCM.Key> by lazy {
    aesGcmKeyGenerator(AES.Key.Size.B192)
}

public val AES_GCM_B256: KeyGenerator<AES.GCM.Key> by lazy {
    aesGcmKeyGenerator(AES.Key.Size.B256)
}

private val AES_GCM_KEY: KeyDecoder<AES.Key.Format, AES.GCM.Key> by lazy {
    AES_GCM.keyDecoder()
}

public suspend fun ByteArray.decodeAESGcmKey(format: AES.Key.Format): AES.GCM.Key = AES_GCM_KEY.decodeFromByteArray(format, this)

public fun ByteArray.decodeAESGcmKeyBlocking(format: AES.Key.Format): AES.GCM.Key = AES_GCM_KEY.decodeFromByteArrayBlocking(format, this)

public suspend fun ByteArray.encrypt(key: AES.GCM.Key, tagSize: BinarySize = 128.bits): ByteArray = key.cipher(tagSize).encrypt(this)

public fun ByteArray.encryptBlocking(key: AES.GCM.Key, tagSize: BinarySize = 128.bits): ByteArray = key.cipher(tagSize).encryptBlocking(this)

public suspend fun ByteArray.decrypt(key: AES.GCM.Key, tagSize: BinarySize = 128.bits): ByteArray = key.cipher(tagSize).decrypt(this)

public fun ByteArray.decryptBlocking(key: AES.GCM.Key, tagSize: BinarySize = 128.bits): ByteArray = key.cipher(tagSize).decryptBlocking(this)
