package klib.data.cache.kache.file

import klib.data.cache.kache.InMemoryKache
import klib.data.cryptography.hashSha256
import klib.data.type.primitives.string.encoding.encodeHexToString

/**
 * A [KeyTransformer] that transforms keys to an SHA-256 hash of them.
 *
 * It caches the hashed keys to avoid recomputing them.
 */
public object SHA256KeyHasher : KeyTransformer {

    private val hashedCache = InMemoryKache<String, String>(maxSize = 1000)

    /**
     * Returns an SHA-256 hash of [oldKey] which may be newly generated or previously cached.
     */
    override suspend fun transform(oldKey: String): String = hashedCache.getOrPut(oldKey) {
        oldKey.encodeToByteArray().hashSha256().encodeHexToString().uppercase()
    }!! // Since our creation function never returns null, we can use not-null assertion
}
