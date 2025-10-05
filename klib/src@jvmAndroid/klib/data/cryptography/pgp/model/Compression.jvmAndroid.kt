package klib.data.cryptography.pgp.model

import klib.data.model.Compression
import org.pgpainless.algorithm.CompressionAlgorithm

internal val COMPRESSIONS = mapOf(
    Compression.UNCOMPRESSED to CompressionAlgorithm.UNCOMPRESSED,
    Compression.ZIP to CompressionAlgorithm.ZIP,
    Compression.ZLIB to CompressionAlgorithm.ZLIB,
    Compression.BZIP2 to CompressionAlgorithm.BZIP2,
)
