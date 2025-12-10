package klib.data.cryptography

import dev.whyoleg.cryptography.random.CryptographyRandom

public fun Boolean.Companion.secureRandom(): Boolean = CryptographyRandom.nextBoolean()

public fun Int.Companion.secureRandomBits(bitCount: Int): Int = CryptographyRandom.nextBits(bitCount)

public fun Int.Companion.secureRandom(): Int = CryptographyRandom.nextInt()

public fun Int.Companion.secureRandom(until: Int): Int = CryptographyRandom.nextInt(until)

public fun Int.Companion.secureRandom(from: Int, until: Int): Int = CryptographyRandom.nextInt(from, until)

public fun Long.Companion.secureRandom(): Long = CryptographyRandom.nextLong()

public fun Long.Companion.secureRandom(until: Long): Long = CryptographyRandom.nextLong(until)

public fun Long.Companion.secureRandom(from: Long, until: Long): Long = CryptographyRandom.nextLong(from, until)

public fun Float.Companion.secureRandom(): Float = CryptographyRandom.nextFloat()

public fun Double.Companion.secureRandom(): Double = CryptographyRandom.nextDouble()

public fun Double.Companion.secureRandom(until: Double): Double = CryptographyRandom.nextDouble(until)

public fun Double.Companion.secureRandom(from: Double, until: Double): Double =
    CryptographyRandom.nextDouble(from, until)

public fun ByteArray.secureRandom(): ByteArray = CryptographyRandom.nextBytes(this)

public fun secureRandomBytes(count: Int): ByteArray = ByteArray(count).secureRandom()

public fun ByteArray.secureRandom(fromIndex: Int, toIndex: Int): ByteArray =
    CryptographyRandom.nextBytes(this, fromIndex, toIndex)

